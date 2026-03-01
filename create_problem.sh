#!/bin/bash
# =============================================================================
# create_problem.sh – Bootstrap a Kotlin DSA problem from a LeetCode number
# =============================================================================
# Usage:   ./create_problem.sh <question_number> [category]
# Example: ./create_problem.sh 1
#          ./create_problem.sh 1 array
#          ./create_problem.sh 206 linked_list
# =============================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROBLEMS_DIR="$SCRIPT_DIR/problems"

# ── helpers ───────────────────────────────────────────────────────────────────
ok()   { echo "✓ $*"; }
info() { echo "→ $*"; }
err()  { echo "✗ $*" >&2; }

usage() {
    cat <<EOF

Usage: $0 <question_number> [category]

  question_number   LeetCode question number (e.g. 1)
  category          (optional) DSA folder name (e.g. array, string, dp)
                    Auto-derived from LeetCode topic tags when omitted.

Examples:
  $0 1
  $0 1 array
  $0 206 linked_list
  $0 20 stack

Supported auto-categories:
  array, string, hash_table, dp, math, sorting, greedy,
  binary_search, two_pointers, sliding_window, stack, queue,
  heap, linked_list, tree, graph, backtracking, bit_manipulation,
  matrix, trie, union_find, divide_and_conquer, misc

EOF
    exit 1
}

# ── validate args ─────────────────────────────────────────────────────────────
QUESTION_NUMBER="${1:-}"
MANUAL_CATEGORY="${2:-}"

if [[ -z "$QUESTION_NUMBER" ]]; then
    err "Question number is required."
    usage
fi

if ! [[ "$QUESTION_NUMBER" =~ ^[0-9]+$ ]]; then
    err "Question number must be numeric."
    usage
fi

# ── check dependencies ────────────────────────────────────────────────────────
for dep in curl python3; do
    if ! command -v "$dep" &>/dev/null; then
        err "'$dep' is required but not installed."
        exit 1
    fi
done

info "Fetching LeetCode problem #$QUESTION_NUMBER …"

# ── fetch & parse via Python ──────────────────────────────────────────────────
TMPJSON="$(mktemp /tmp/lc_problem_XXXXXX.json)"

python3 - "$QUESTION_NUMBER" > "$TMPJSON" <<'PYEOF'
import sys, json, re, html as html_mod, urllib.request, urllib.error

GRAPHQL = "https://leetcode.com/graphql"
QID     = sys.argv[1]
HDRS    = {
    "Content-Type": "application/json",
    "User-Agent"  : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36",
    "Referer"     : "https://leetcode.com",
}

def gql(payload):
    body = json.dumps(payload).encode()
    req  = urllib.request.Request(GRAPHQL, data=body, headers=HDRS, method="POST")
    try:
        with urllib.request.urlopen(req, timeout=20) as r:
            return json.loads(r.read())
    except urllib.error.HTTPError as e:
        sys.exit(f"HTTP error {e.code}: {e.reason}")
    except Exception as e:
        sys.exit(f"Network error: {e}")

# ── Step 1: Find titleSlug from question number ───────────────────────────────
slug_resp = gql({
    "query": """
      query($filters: QuestionListFilterInput) {
        questionList(categorySlug: "" limit: 50 skip: 0 filters: $filters) {
          data { questionFrontendId titleSlug }
        }
      }
    """,
    "variables": {"filters": {"searchKeywords": QID}}
})

questions = slug_resp.get("data", {}).get("questionList", {}).get("data", [])
slug = next(
    (q["titleSlug"] for q in questions if str(q["questionFrontendId"]) == QID),
    None
)

if not slug:
    sys.exit(f"Question #{QID} not found on LeetCode. Check the number and try again.")

# ── Step 2: Fetch full question details ───────────────────────────────────────
det_resp = gql({
    "query": """
      query($s: String!) {
        question(titleSlug: $s) {
          questionFrontendId title difficulty
          content
          topicTags { name slug }
          codeSnippets { lang langSlug code }
        }
      }
    """,
    "variables": {"s": slug}
})

q = det_resp.get("data", {}).get("question")
if not q:
    sys.exit(f"Could not load details for question #{QID}.")

# ── Step 3: Clean HTML content ────────────────────────────────────────────────
def clean_html(raw):
    t = raw or ""
    t = re.sub(r'<br\s*/?>', '\n',   t, flags=re.I)
    t = re.sub(r'</p>',      '\n',   t, flags=re.I)
    t = re.sub(r'<p[^>]*>',  '',     t, flags=re.I)
    t = re.sub(r'<li[^>]*>',  '- ',  t, flags=re.I)
    t = re.sub(r'</li>',      '\n',  t, flags=re.I)
    t = re.sub(r'</?[uo]l[^>]*>', '\n', t, flags=re.I)
    t = re.sub(r'<pre[^>]*>(.*?)</pre>', r'\n\1\n', t, flags=re.I | re.S)
    t = re.sub(r'<code[^>]*>(.*?)</code>', r'`\1`', t, flags=re.I | re.S)
    t = re.sub(r'<sup[^>]*>(.*?)</sup>',   r'^\1',  t, flags=re.I | re.S)
    t = re.sub(r'<sub[^>]*>(.*?)</sub>',   r'_\1',  t, flags=re.I | re.S)
    t = re.sub(r'<strong[^>]*>(.*?)</strong>', r'\1', t, flags=re.I | re.S)
    t = re.sub(r'<em[^>]*>(.*?)</em>',        r'\1', t, flags=re.I | re.S)
    t = re.sub(r'<[^>]+>', '', t)
    t = html_mod.unescape(t)
    t = re.sub(r'[ \t]+',   ' ',  t)
    t = re.sub(r'\n{3,}', '\n\n', t)
    return '\n'.join(l.rstrip() for l in t.splitlines()).strip()

content = clean_html(q.get("content", ""))

# Split into description, examples, constraints
desc_part  = re.split(r'\n\s*Example\s+\d+', content, maxsplit=1)[0].strip()
examples   = re.findall(
    r'(Example\s+\d+:.*?)(?=\nExample\s+\d+:|\nConstraints|\Z)',
    content, re.S
)
cons_match = re.search(r'Constraints:?\n(.*)', content, re.S)
consts     = cons_match.group(1).strip() if cons_match else ""

# ── Step 4: Kotlin snippet ────────────────────────────────────────────────────
kt_snippet = next(
    (s["code"] for s in (q.get("codeSnippets") or []) if s["langSlug"] == "kotlin"),
    "class Solution {\n    fun solution(): Unit {\n        TODO()\n    }\n}"
)

# ── Step 5: Category from topic tags ─────────────────────────────────────────
TAG_MAP = {
    "array":               "array",
    "string":              "string",
    "hash-table":          "hash_table",
    "dynamic-programming": "dp",
    "math":                "math",
    "sorting":             "sorting",
    "greedy":              "greedy",
    "depth-first-search":  "graph",
    "breadth-first-search":"graph",
    "binary-search":       "binary_search",
    "tree":                "tree",
    "binary-tree":         "tree",
    "binary-search-tree":  "tree",
    "graph":               "graph",
    "linked-list":         "linked_list",
    "two-pointers":        "two_pointers",
    "stack":               "stack",
    "queue":               "queue",
    "monotonic-stack":     "stack",
    "heap-priority-queue": "heap",
    "sliding-window":      "sliding_window",
    "backtracking":        "backtracking",
    "bit-manipulation":    "bit_manipulation",
    "matrix":              "matrix",
    "trie":                "trie",
    "union-find":          "union_find",
    "recursion":           "array",
    "divide-and-conquer":  "divide_and_conquer",
    "prefix-sum":          "array",
    "segment-tree":        "tree",
}

tag_names  = [t["name"] for t in (q.get("topicTags") or [])]
tag_slugs  = [t["slug"] for t in (q.get("topicTags") or [])]
category   = next((TAG_MAP[s] for s in tag_slugs if s in TAG_MAP), "misc")

print(json.dumps({
    "id":          q["questionFrontendId"],
    "title":       q["title"],
    "difficulty":  q["difficulty"],
    "tags":        tag_names,
    "slug":        slug,
    "category":    category,
    "description": desc_part,
    "examples":    [e.strip() for e in examples],
    "constraints": consts,
    "kt_snippet":  kt_snippet,
}, ensure_ascii=False))
PYEOF

PYEXIT=$?

if [[ $PYEXIT -ne 0 ]] || [[ ! -s "$TMPJSON" ]]; then
    err "Failed to fetch problem data. Check your internet connection."
    rm -f "$TMPJSON"
    exit 1
fi

# ── read fields from JSON ─────────────────────────────────────────────────────
_py() { python3 -c "import json,sys; d=json.load(open(sys.argv[1])); $1" "$TMPJSON"; }

TITLE=$(_py "print(d['title'])")
DIFFICULTY=$(_py "print(d['difficulty'])")
TAGS=$(_py "print(', '.join(d['tags']))")
SLUG=$(_py "print(d['slug'])")
AUTO_CAT=$(_py "print(d['category'])")
CATEGORY="${MANUAL_CATEGORY:-$AUTO_CAT}"

ok "Fetched : #$QUESTION_NUMBER – $TITLE ($DIFFICULTY)"
info "Category : $CATEGORY"
info "Tags     : $TAGS"

# ── derive class name and folder name ─────────────────────────────────────────
# PascalCase: strip non-alphanumeric, capitalize each word, join
PASCAL=$(echo "$TITLE" \
    | sed 's/[^a-zA-Z0-9 ]/ /g' \
    | awk '{for(i=1;i<=NF;i++){$i=toupper(substr($i,1,1)) substr($i,2)}; print}' \
    | tr -d ' ')

FORMATTED_NUM=$(printf "%04d" "$QUESTION_NUMBER")
FOLDER_NAME="LC${FORMATTED_NUM}_${PASCAL}"
FOLDER_PATH="$PROBLEMS_DIR/$CATEGORY/$FOLDER_NAME"

info "Location : $FOLDER_PATH"

# ── guard: folder already exists ──────────────────────────────────────────────
if [[ -d "$FOLDER_PATH" ]]; then
    err "Folder already exists: $FOLDER_PATH"
    rm -f "$TMPJSON"
    exit 1
fi

mkdir -p "$FOLDER_PATH"

# ── generate Kotlin file via Python ───────────────────────────────────────────
KOTLIN_FILE="$FOLDER_PATH/${PASCAL}.kt"

python3 - "$TMPJSON" "$KOTLIN_FILE" <<'PYEOF'
import json, re, sys, textwrap

data    = json.load(open(sys.argv[1]))
outpath = sys.argv[2]

id_    = data["id"]
title  = data["title"]
diff   = data["difficulty"]
tags   = ", ".join(data["tags"])
slug   = data["slug"]
desc   = data["description"]
exs    = data["examples"]
consts = data["constraints"]
snip   = data["kt_snippet"]

# ── build KDoc comment ────────────────────────────────────────────────────────
def wrap_comment(text, width=92):
    """Wrap plain text to fit inside a KDoc block comment."""
    result = []
    for para in text.splitlines():
        para = para.rstrip()
        if not para:
            result.append(" *")
            continue
        for line in textwrap.wrap(para, width - 3) or [para]:
            result.append(f" * {line}")
    return "\n".join(result)

url = f"https://leetcode.com/problems/{slug}/"

lines = [
    "/**",
    f" * LeetCode #{id_} - {title}",
    f" * Difficulty : {diff}",
    f" * Topics     : {tags}",
    f" * URL        : {url}",
    " *",
    " * Problem:",
]

lines.append(wrap_comment(desc))

for i, ex in enumerate(exs, 1):
    lines.append(" *")
    lines.append(f" * Example {i}:")
    for ln in ex.splitlines():
        stripped = ln.strip()
        if stripped and not stripped.lower().startswith("example"):
            lines.append(f" *   {stripped}")

if consts:
    lines.append(" *")
    lines.append(" * Constraints:")
    for ln in consts.splitlines():
        stripped = ln.strip()
        if stripped:
            lines.append(f" *   {stripped}")

lines.append(" */")
kdoc = "\n".join(lines)

# ── extract inner body from snippet ──────────────────────────────────────────
# Strip the outer "class Solution { ... }" wrapper
inner_match = re.search(r'class\s+Solution\s*\{(.*)\}', snip, re.DOTALL)
inner = inner_match.group(1) if inner_match else (
    "\n    fun solution(): Unit {\n        TODO()\n    }\n"
)

# Extract first function name for main()
fn_match = re.search(r'fun\s+(\w+)', inner)
fn_name  = fn_match.group(1) if fn_match else "solution"

# Inject Time/Space complexity comment before the first `fun`
inner_annotated = re.sub(
    r'(\n    fun )',
    '\n    // Time: O(?)\n    // Space: O(?)\n    fun ',
    inner,
    count=1
)

# ── assemble final Kotlin source ──────────────────────────────────────────────
kt = (
    kdoc + "\n"
    "class Solution {\n"
    + inner_annotated.rstrip() + "\n"
    "}\n"
    "\n"
    "fun main() {\n"
    "    val solution = Solution()\n"
    "\n"
    "    // TODO: Add test cases\n"
    f"    // val result = solution.{fn_name}(...)\n"
    '    // println("Output: $result")\n'
    "}\n"
)

with open(outpath, "w") as f:
    f.write(kt)
PYEOF

ok "Created ${PASCAL}.kt"

# ── update IntelliJ module files ──────────────────────────────────────────────
IML_FILE="$PROBLEMS_DIR/$CATEGORY/${CATEGORY}.iml"
MODULES_XML="$SCRIPT_DIR/.idea/modules.xml"

python3 - "$IML_FILE" "$FOLDER_NAME" "$MODULES_XML" "$CATEGORY" <<'PYEOF'
import sys, os

iml_path    = sys.argv[1]
folder_name = sys.argv[2]
modules_xml = sys.argv[3]
category    = sys.argv[4]

new_source_line = f'      <sourceFolder url="file://$MODULE_DIR$/{folder_name}" isTestSource="false" />'

# ── Create or update category .iml ───────────────────────────────────────────
if os.path.exists(iml_path):
    with open(iml_path) as f:
        content = f.read()
    if folder_name not in content:
        content = content.replace('    </content>', f'{new_source_line}\n    </content>')
        with open(iml_path, 'w') as f:
            f.write(content)
else:
    iml_content = f'''<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content url="file://$MODULE_DIR$">
{new_source_line}
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
  </component>
</module>
'''
    with open(iml_path, 'w') as f:
        f.write(iml_content)

# ── Add module to modules.xml if not already there ────────────────────────────
module_entry = f'$PROJECT_DIR$/problems/{category}/{category}.iml'
with open(modules_xml) as f:
    mx = f.read()
if module_entry not in mx:
    new_module = f'      <module fileurl="file://{module_entry}" filepath="{module_entry}" />'
    mx = mx.replace('    </modules>', f'{new_module}\n    </modules>')
    with open(modules_xml, 'w') as f:
        f.write(mx)
PYEOF

ok "Updated ${CATEGORY}.iml"

# ── cleanup & summary ─────────────────────────────────────────────────────────
rm -f "$TMPJSON"

echo ""
ok "Problem scaffolded successfully!"
echo ""
echo "   #$QUESTION_NUMBER  $TITLE  ($DIFFICULTY)"
echo "   $FOLDER_PATH"
echo ""
echo "Next steps:"
echo "  1. Open ${PASCAL}.kt and implement the solution"
echo "  2. Add test cases in main()"
echo "  3. Hit the ▶ button next to fun main() in IntelliJ to run"
echo ""

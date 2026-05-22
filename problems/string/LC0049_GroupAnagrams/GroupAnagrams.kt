/**
 * LeetCode #49 - Group Anagrams
 * Difficulty : Medium
 * Topics     : Array, Hash Table, String, Sorting
 * URL        : https://leetcode.com/problems/group-anagrams/
 *
 * Problem:
 * Given an array of strings `strs`, group the anagrams together. You can return the answer
 * in any order.
 *
 * Example 1:
 *   Input: strs = ["eat","tea","tan","ate","nat","bat"]
 *   Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *   Explanation:
 *   - There is no string in strs that can be rearranged to form `"bat"`.
 *   - The strings `"nat"` and `"tan"` are anagrams as they can be rearranged to form each other.
 *   - The strings `"ate"`, `"eat"`, and `"tea"` are anagrams as they can be rearranged to form each other.
 *
 * Example 2:
 *   Input: strs = [""]
 *   Output: [[""]]
 *
 * Example 3:
 *   Input: strs = ["a"]
 *   Output: [["a"]]
 *
 * Constraints:
 *   - `1 <= strs.length <= 10^4`
 *   - `0 <= strs[i].length <= 100`
 *   - `strs[i]` consists of lowercase English letters.
 */
class GroupAnagramsSolution {

    // Time: O(?)
    // Space: O(?)
    fun groupAnagrams(strs: Array<String>): List<List<String>> {
        val map = HashMap<String, ArrayList<String>>()

        for (s in strs) {
            val key = s.toCharArray().sorted().joinToString("")
            if (!map.contains(key)) {
                map[key] = arrayListOf()
            }
            map[key]?.add(s)

        }
        return map.values.toList()
    }
}

fun main() {
    val solution = GroupAnagramsSolution()

    val testCases = arrayOf(
        listOf("eat","tea","tan","ate","nat","bat"),
        listOf(""),
        listOf("a")
    )

    for (list in testCases) {
        val result = solution.groupAnagrams(list.toTypedArray())
        println("Input: [\"${list.joinToString()}\"]")
        println("-------------")
        println("Output: [${result.joinToString()}]")
        println("----------------------------------")
        println("----------------------------------")
    }
}

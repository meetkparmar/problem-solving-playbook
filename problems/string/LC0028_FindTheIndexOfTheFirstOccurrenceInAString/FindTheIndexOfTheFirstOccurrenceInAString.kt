/**
 * LeetCode #28 - Find the Index of the First Occurrence in a String
 * Difficulty : Easy
 * Topics     : Two Pointers, String, String Matching
 * URL        : https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
 *
 * Problem:
 * Given two strings `needle` and `haystack`, return the index of the first occurrence of
 * `needle` in `haystack`, or `-1` if `needle` is not part of `haystack`.
 *
 * Example 1:
 *   Input: haystack = "sadbutsad", needle = "sad"
 *   Output: 0
 *   Explanation: "sad" occurs at index 0 and 6.
 *   The first occurrence is at index 0, so we return 0.
 *
 * Example 2:
 *   Input: haystack = "leetcode", needle = "leeto"
 *   Output: -1
 *   Explanation: "leeto" did not occur in "leetcode", so we return -1.
 *
 * Constraints:
 *   - `1 <= haystack.length, needle.length <= 10^4`
 *   - `haystack` and `needle` consist of only lowercase English characters.
 */
class FindTheIndexOfTheFirstOccurrenceInAStringSolution {

    // Time: O(n+m)
    // Space: O(1)
    fun strStr(haystack: String, needle: String): Int {
        for(i in 0..haystack.length - needle.length){
            if(needle[0] == haystack[i]) {
                if(haystack.substring(i, (i + needle.length)) == needle){
                    return i
                }
            }
        }
        return -1
    }
}

fun main() {
    val solution = FindTheIndexOfTheFirstOccurrenceInAStringSolution()

    val testCases = listOf(
        Pair("sadbutsad", "sad"),
        Pair("leetcode", "leeto"),
    )

    for ((s, t) in testCases) {
        val result = solution.strStr(s, t)
        println("Input: \"$s\", \"$t\"")
        println("-------------")
        println("Output: $result")
        println("----------------------------------")
        println("----------------------------------")
    }

}

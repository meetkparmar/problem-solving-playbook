/**
 * LeetCode #242 - Valid Anagram
 * Difficulty : Easy
 * Topics     : Hash Table, String, Sorting
 * URL        : https://leetcode.com/problems/valid-anagram/
 *
 * Problem:
 * Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and `false`
 * otherwise.
 *
 * Example 1:
 *   Input: s = "anagram", t = "nagaram"
 *   Output: true
 *
 * Example 2:
 *   Input: s = "rat", t = "car"
 *   Output: false
 *
 * Constraints:
 *   - `1 <= s.length, t.length <= 5 * 10^4`
 *   - `s` and `t` consist of lowercase English letters.
 *   Follow up: What if the inputs contain Unicode characters? How would you adapt your solution to such a case?
 */
class ValidAnagramSolution {

    // Time: O(n)
    // Space: O(n)
    fun isAnagram(s: String, t: String): Boolean {
        if(s.length != t.length) return false
        val list = IntArray(26)

        val n = s.length-1

        for(i in 0..n) {
            list[s[i] - 'a']++
            list[t[i] - 'a']--
        }

        return list.all { it == 0 }
    }
}

fun main() {
    val solution = ValidAnagramSolution()

    val testCases = listOf(
        Pair("anagram", "nagaram"),
        Pair("rat", "car"),
    )

    for ((s, t) in testCases) {
        val result = solution.isAnagram(s, t)
        println("Input: \"$s\", \"$t\"")
        println("-------------")
        println("Output: $result")
        println("----------------------------------")
        println("----------------------------------")
    }
}

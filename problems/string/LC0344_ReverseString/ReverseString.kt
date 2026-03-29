/**
 * LeetCode #344 - Reverse String
 * Difficulty : Easy
 * Topics     : Two Pointers, String
 * URL        : https://leetcode.com/problems/reverse-string/
 *
 * Problem:
 * Write a function that reverses a string. The input string is given as an array of
 * characters `s`.
 *
 * You must do this by modifying the input array in-place with `O(1)` extra memory.
 *
 * Example 1:
 *   Input: s = ["h","e","l","l","o"]
 *   Output: ["o","l","l","e","h"]
 *
 * Example 2:
 *   Input: s = ["H","a","n","n","a","h"]
 *   Output: ["h","a","n","n","a","H"]
 *
 * Constraints:
 *   - `1 <= s.length <= 10^5`
 *   - `s[i]` is a printable ascii character.
 */
class ReverseStringSolution {

    // Time: O(?)
    // Space: O(?)
    fun reverseString(s: CharArray): CharArray {
        var l = 0
        var r = s.size-1

        while(l<r) {
            val temp = s[l]
            s[l] = s[r]
            s[r] = temp
            l++
            r--
        }
        return s
    }
}

fun main() {
    val solution = ReverseStringSolution()

    val testCases = listOf(
        charArrayOf('h','e','l','l','o'),
        charArrayOf('H','a','n','n','a','h')
    )

    for (str in testCases) {
        val result = solution.reverseString(str)
        println("Input: [${str.joinToString()}]")
        println("-------------")
        println("Output: [${result.joinToString()}]")
        println("----------------------------------")
        println("----------------------------------")
    }
}

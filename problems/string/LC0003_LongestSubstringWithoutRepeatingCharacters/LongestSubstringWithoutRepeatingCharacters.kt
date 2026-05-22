import kotlin.math.max

/**
 * LeetCode #3 - Longest Substring Without Repeating Characters
 * Difficulty : Medium
 * Topics     : Hash Table, String, Sliding Window
 * URL        : https://leetcode.com/problems/longest-substring-without-repeating-characters/
 *
 * Problem:
 * Given a string `s`, find the length of the longest substring without duplicate
 * characters.
 *
 * Example 1:
 *   Input: s = "abcabcbb"
 *   Output: 3
 *   Explanation: The answer is "abc", with the length of 3. Note that `"bca"` and `"cab"` are also correct answers.
 *
 * Example 2:
 *   Input: s = "bbbbb"
 *   Output: 1
 *   Explanation: The answer is "b", with the length of 1.
 *
 * Example 3:
 *   Input: s = "pwwkew"
 *   Output: 3
 *   Explanation: The answer is "wke", with the length of 3.
 *   Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
 *
 * Constraints:
 *   - `0 <= s.length <= 5 * 10^4`
 *   - `s` consists of English letters, digits, symbols and spaces.
 */
class LongestSubstringWithoutRepeatingCharactersSolution {

    // Time: O(?)
    // Space: O(?)
    fun lengthOfLongestSubstring(s: String): Int {
        if(s.isEmpty()) return 0
        var max = 1
        for(i in s.indices) {
            var tempMax = 0
            val set = mutableSetOf<Char>()
            for(j in i until s.length) {
                if(set.contains(s[j])) {
                    break
                } else {
                    tempMax++
                    set.add(s[j])
                }
            }
            max = max(max, tempMax)
        }

        return max
    }
}

fun main() {
    val solution = LongestSubstringWithoutRepeatingCharactersSolution()

    val testCases = listOf(
        "aab",
        "abcabcbb",
        "bbbbb",
        "pwwkew"
    )

    for (str in testCases) {
        val result = solution.lengthOfLongestSubstring(str)
        println("Input: \"$str\"")
        println("-------------")
        println("Output: $result")
        println("----------------------------------")
        println("----------------------------------")
    }
}

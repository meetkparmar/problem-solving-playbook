/**
 * LeetCode #387 - First Unique Character in a String
 * Difficulty : Easy
 * Topics     : Hash Table, String, Queue, Counting
 * URL        : https://leetcode.com/problems/first-unique-character-in-a-string/
 *
 * Problem:
 * Given a string `s`, find the first non-repeating character in it and return its index. If
 * it does not exist, return `-1`.
 *
 * Example 1:
 *   Input: s = "leetcode"
 *   Output: 0
 *   Explanation:
 *   The character `'l'` at index 0 is the first character that does not occur at any other index.
 *
 * Example 2:
 *   Input: s = "loveleetcode"
 *   Output: 2
 *
 * Example 3:
 *   Input: s = "aabb"
 *   Output: -1
 *
 * Constraints:
 *   - `1 <= s.length <= 10^5`
 *   - `s` consists of only lowercase English letters.
 */
class FirstUniqueCharacterInAStringSolution {

    // Time: O(1)
    // Space: O(1)
    fun firstUniqChar(s: String): Int {
        val count = IntArray(26)

        for (ch in s) {
            count[ch - 'a']++
        }

        for (i in s.indices) {
            if (count[s[i] - 'a'] == 1) {
                return i
            }
        }

        return -1
    }
}

fun main() {
    val solution = FirstUniqueCharacterInAStringSolution()

    val testCases = listOf(
        "leetcode",
        "loveleetcode",
        "aabb"
    )

    for (str in testCases) {
        val result = solution.firstUniqChar(str)
        println("Input: \"$str\"")
        println("-------------")
        println("Output: $result")
        println("----------------------------------")
        println("----------------------------------")
    }
}

import java.lang.IO.println

/**
 * LeetCode #383 - Ransom Note
 * Difficulty : Easy
 * Topics     : Hash Table, String, Counting
 * URL        : https://leetcode.com/problems/ransom-note/
 *
 * Problem:
 * Given two strings `ransomNote` and `magazine`, return `true` if `ransomNote` can be
 * constructed by using the letters from `magazine` and `false` otherwise.
 *
 * Each letter in `magazine` can only be used once in `ransomNote`.
 *
 * Example 1:
 *   Input: ransomNote = "a", magazine = "b"
 *   Output: false
 *
 * Example 2:
 *   Input: ransomNote = "aa", magazine = "ab"
 *   Output: false
 *
 * Example 3:
 *   Input: ransomNote = "aa", magazine = "aab"
 *   Output: true
 *
 * Constraints:
 *   - `1 <= ransomNote.length, magazine.length <= 10^5`
 *   - `ransomNote` and `magazine` consist of lowercase English letters.
 */
class RansomNoteSolution {

    // Time: O(?)
    // Space: O(?)
    fun canConstruct(ransomNote: String, magazine: String): Boolean {
        val map = hashMapOf<Char, Int>()
        ransomNote.forEach {
            map[it] = map[it]?.plus(1) ?: 1
        }

        magazine.forEach {
            if(map.contains(it)) {
                map[it] = map[it]!!.minus(1)
            }
        }

        map.forEach {
            if(it.value > 0) return false
        }

        return true
    }
}

fun main() {
    val solution = RansomNoteSolution()

    val testCases = listOf(
        Pair("a", "b"),
        Pair("aa", "ab"),
        Pair("aa", "aab")
    )

    testCases.forEach { (s, t) ->
        val result = solution.canConstruct(s, t)
        println("Input: \"$s\", \"$t\"")
        println("-------------")
        println("Output: $result")
        println("----------------------------------")
        println("----------------------------------")
    }
}

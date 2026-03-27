/**
 * LeetCode #448 - Find All Numbers Disappeared in an Array
 * Difficulty : Easy
 * Topics     : Array, Hash Table
 * URL        : https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
 *
 * Problem:
 * Given an array `nums` of `n` integers where `nums[i]` is in the range `[1, n]`, return an
 * array of all the integers in the range `[1, n]` that do not appear in `nums`.
 *
 * Example 1:
 *   Input: nums = [4,3,2,7,8,2,3,1]
 *   Output: [5,6]
 *
 * Example 2:
 *   Input: nums = [1,1]
 *   Output: [2]
 *
 * Constraints:
 *   - `n == nums.length`
 *   - `1 <= n <= 10^5`
 *   - `1 <= nums[i] <= n`
 *   Follow up: Could you do it without extra space and in `O(n)` runtime? You may assume the returned list does not count as extra space.
 */
class DisappearedNumberSolution {

    // Time: O(n)
    // Space: O(n)
    fun findDisappearedNumbers(nums: IntArray): List<Int> {
        for (num in nums) {
            val idx = kotlin.math.abs(num) - 1
            if (nums[idx] > 0) {
                nums[idx] = -nums[idx]
            }
        }

        val result = mutableListOf<Int>()
        for (i in nums.indices) {
            if (nums[i] > 0) {
                result.add(i + 1)
            }
        }
        return result
    }
}

fun main() {
    val solution = DisappearedNumberSolution()

    val testCases = listOf(
        intArrayOf(4,3,2,7,8,2,3,1),
        intArrayOf(1,1)
    )

    for (nums in testCases) {
        val result = solution.findDisappearedNumbers(nums)
        println("Input: [${nums.joinToString()}]")
        println("-------------")
        println("Output: [${result.joinToString()}]")
        println("----------------------------------")
        println("----------------------------------")
    }
}

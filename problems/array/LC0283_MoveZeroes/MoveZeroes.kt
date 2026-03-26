/**
 * LeetCode #283 - Move Zeroes
 * Difficulty : Easy
 * Topics     : Array, Two Pointers
 * URL        : https://leetcode.com/problems/move-zeroes/
 *
 * Problem:
 * Given an integer array `nums`, move all `0`'s to the end of it while maintaining the
 * relative order of the non-zero elements.
 *
 * Note that you must do this in-place without making a copy of the array.
 *
 * Example 1:
 *   Input: nums = [0,1,0,3,12]
 *   Output: [1,3,12,0,0]
 *
 * Example 2:
 *   Input: nums = [0]
 *   Output: [0]
 *
 * Constraints:
 *   - `1 <= nums.length <= 10^4`
 *   - `-2^31 <= nums[i] <= 2^31 - 1`
 *   Follow up: Could you minimize the total number of operations done?
 */
class MoveZeroesSolution {

    // Time: O(n)
    // Space: O(1)
    fun moveZeroes(nums: IntArray): IntArray {
        var i = 0

        for(j in nums.indices) {
            if(nums[j] != 0) {
                val temp = nums[j]
                nums[j] = nums[i]
                nums[i] = temp
                i++
            }
        }
        return nums
    }
}

fun main() {
    val solution = MoveZeroesSolution()

    val testCases = listOf(
        intArrayOf(0,1,0,3,12),
        intArrayOf(0)
    )

    for (nums in testCases) {
        val result = solution.moveZeroes(nums)
        println("Input: [${nums.joinToString()}]")
        println("-------------")
        println("Output: [${result.joinToString()}]")
        println("----------------------------------")
        println("----------------------------------")
    }
}

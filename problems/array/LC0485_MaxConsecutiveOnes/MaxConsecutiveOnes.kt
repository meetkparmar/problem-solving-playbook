import kotlin.math.max

/**
 * LeetCode #485 - Max Consecutive Ones
 * Difficulty : Easy
 * Topics     : Array
 * URL        : https://leetcode.com/problems/max-consecutive-ones/
 *
 * Problem:
 * Given a binary array `nums`, return the maximum number of consecutive `1`'s in the array.
 *
 * Example 1:
 *   Input: nums = [1,1,0,1,1,1]
 *   Output: 3
 *   Explanation: The first two digits or the last three digits are consecutive 1s. The maximum number of consecutive 1s is 3.
 *
 * Example 2:
 *   Input: nums = [1,0,1,1,0,1]
 *   Output: 2
 *
 * Constraints:
 *   - `1 <= nums.length <= 10^5`
 *   - `nums[i]` is either `0` or `1`.
 */
class MaxConsecutiveOnesSolution {

    // Time: O(n)
    // Space: O(1)
    fun findMaxConsecutiveOnes(nums: IntArray): Int {
        var count = 0
        var currCount = 0

        for(n in nums) {
            if(n == 1) {
                currCount++
            } else {
                count = max(count, currCount)
                currCount = 0
            }
        }
        return max(count, currCount)
    }
}

fun main() {
    val solution = MaxConsecutiveOnesSolution()

    val testCases = listOf(
        intArrayOf(1,1,0,1,1,1),
        intArrayOf(1,0,1,1,0,1)
    )

    for (nums in testCases) {
        val result = solution.findMaxConsecutiveOnes(nums)
        println("Input: [${nums.joinToString()}]")
        println("-------------")
        println("Output: ${result}")
        println("----------------------------------")
        println("----------------------------------")
    }
}

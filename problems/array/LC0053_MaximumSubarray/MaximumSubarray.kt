import kotlin.math.max

/**
 * LeetCode #53 - Maximum Subarray
 * Difficulty : Medium
 * Topics     : Array, Divide and Conquer, Dynamic Programming
 * URL        : https://leetcode.com/problems/maximum-subarray/
 *
 * Problem:
 * Given an integer array `nums`, find the subarray with the largest sum, and return its
 * sum.
 *
 * Example 1:
 *   Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
 *   Output: 6
 *   Explanation: The subarray [4,-1,2,1] has the largest sum 6.
 *
 * Example 2:
 *   Input: nums = [1]
 *   Output: 1
 *   Explanation: The subarray [1] has the largest sum 1.
 *
 * Example 3:
 *   Input: nums = [5,4,-1,7,8]
 *   Output: 23
 *   Explanation: The subarray [5,4,-1,7,8] has the largest sum 23.
 *
 * Constraints:
 *   - `1 <= nums.length <= 10^5`
 *   - `-10^4 <= nums[i] <= 10^4`
 *   Follow up: If you have figured out the `O(n)` solution, try coding another solution using the divide and conquer approach, which is more subtle.
 */
class MaximumSubarraySolution {

    // Time: O(n)
    // Space: O(n)
    fun maxSubArray(nums: IntArray): Int {
        var currentSum = nums[0]
        var maxSum = currentSum

        for (i in 1 until nums.size) {
            currentSum = max(nums[i], currentSum + nums[i])
            maxSum = max(currentSum, maxSum)
        }

        return maxSum
    }
}

fun main() {
    val solution = MaximumSubarraySolution()

    val testCases = listOf(
        intArrayOf(-2,1,-3,4,-1,2,1,-5,4),
        intArrayOf(1),
        intArrayOf(5,4,-1,7,8)
    )

    for (nums in testCases) {
        val result = solution.maxSubArray(nums)
        println("Input: [${nums.joinToString()}]")
        println("-------------")
        println("Output: $result")
        println("----------------------------------")
        println("----------------------------------")
    }
}

/**
 * LeetCode #35 - Search Insert Position
 * Difficulty : Easy
 * Topics     : Array, Binary Search
 * URL        : https://leetcode.com/problems/search-insert-position/
 *
 * Problem:
 * Given a sorted array of distinct integers and a target value, return the index if the
 * target is found. If not, return the index where it would be if it were inserted in order.
 *
 * You must write an algorithm with `O(log n)` runtime complexity.
 *
 * Example 1:
 *   Input: nums = [1,3,5,6], target = 5
 *   Output: 2
 *
 * Example 2:
 *   Input: nums = [1,3,5,6], target = 2
 *   Output: 1
 *
 * Example 3:
 *   Input: nums = [1,3,5,6], target = 7
 *   Output: 4
 *
 * Constraints:
 *   - `1 <= nums.length <= 10^4`
 *   - `-10^4 <= nums[i] <= 10^4`
 *   - `nums` contains distinct values sorted in ascending order.
 *   - `-10^4 <= target <= 10^4`
 */
class SearchInsertPositionSolution {

    // Time: O(log(n))
    // Space: O(1)
    fun searchInsert(nums: IntArray, target: Int): Int {
        var left = 0
        var right = nums.size-1
        var mid = 0
        if(nums[left] > target) {
            return left
        } else if (nums[right] < target) {
            return right+1
        } else {
            while (left <= right) {
                mid = left + (right-left)/2

                if(nums[mid] == target) {
                    return mid
                } else if (left+1 == right) {
                    return right
                } else if (nums[mid] < target) {
                    left = mid
                } else {
                    right = mid
                }
            }
            return mid
        }
    }
}

fun main() {
    val solution = SearchInsertPositionSolution()

    val testCases = listOf(
        Pair(intArrayOf(1,3,5,6), 5),
        Pair(intArrayOf(1,3,5,6), 2),
        Pair(intArrayOf(1,3,5,6), 7)
    )

    for ((nums, target) in testCases) {
        val result = solution.searchInsert(nums, target)
        println("Input: [${nums.joinToString()}]\nTarget: $target")
        println("-------------")
        println("Output: $result")
        println("----------------------------------")
        println("----------------------------------")
    }
}

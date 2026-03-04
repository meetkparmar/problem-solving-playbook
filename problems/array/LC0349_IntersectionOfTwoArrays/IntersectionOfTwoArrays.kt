/**
 * LeetCode #349 - Intersection of Two Arrays
 * Difficulty : Easy
 * Topics     : Array, Hash Table, Two Pointers, Binary Search, Sorting
 * URL        : https://leetcode.com/problems/intersection-of-two-arrays/
 *
 * Problem:
 * Given two integer arrays `nums1` and `nums2`, return an array of their intersection. Each
 * element in the result must be unique and you may return the result in any order.
 *
 * Example 1:
 *   Input: nums1 = [1,2,2,1], nums2 = [2,2]
 *   Output: [2]
 *
 * Example 2:
 *   Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 *   Output: [9,4]
 *   Explanation: [4,9] is also accepted.
 *
 * Constraints:
 *   - `1 <= nums1.length, nums2.length <= 1000`
 *   - `0 <= nums1[i], nums2[i] <= 1000`
 */
class IntersectionOfArraySolution {

    // Time: O(n + m)
    // Space: O(n + m)
    fun intersection(nums1: IntArray, nums2: IntArray): IntArray {
        val set1 = nums1.toHashSet()
        val result = mutableSetOf<Int>()
        for (num in nums2) {
            if (num in set1) result.add(num)
        }
        return result.toIntArray()
    }
}

fun main() {
    val solution = IntersectionOfArraySolution()

    val testCases = listOf(
        Pair(intArrayOf(1, 2, 2, 1), intArrayOf(2, 2)),
        Pair(intArrayOf(4, 9, 5), intArrayOf(9, 4, 9, 8, 4))
    )

    for ((nums1, nums2) in testCases) {
        val result = solution.intersection(nums1, nums2)
        println("Input: [${nums1.joinToString()}]\n [${nums2.joinToString()}]")
        println("-------------")
        println("Output: ${result.joinToString()}")
        println("----------------------------------")
        println("----------------------------------")
    }
}

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class RoomsWithMostMeetings {

    public int mostBooked(int n, int[][] meetings) {
        // Sort meetings by their start time
        Arrays.sort(meetings, Comparator.comparingInt(a -> a[0]));

        // Priority Queue for tracking idle room indices
        PriorityQueue<Integer> idleRooms = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            idleRooms.offer(i);
        }

        // Priority Queue for tracking busy rooms by their availability time, then room index
        PriorityQueue<int[]> busyRooms = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1];
        });

        // Array to count bookings for each room
        int[] bookingCounts = new int[n];

        for (int[] meeting : meetings) {
            // Free up rooms that have become available
            while (!busyRooms.isEmpty() && busyRooms.peek()[0] <= meeting[0]) {
                idleRooms.offer(busyRooms.poll()[1]);
            }

            if (!idleRooms.isEmpty()) {
                int roomId = idleRooms.poll();
                bookingCounts[roomId]++;
                busyRooms.offer(new int[]{meeting[1], roomId});
            } else {
                int[] busyRoom = busyRooms.poll();
                bookingCounts[busyRoom[1]]++;
                // Extend the busy room's booking time
                busyRoom[0] += meeting[1] - meeting[0];
                busyRooms.offer(busyRoom);
            }
        }

        // Determine the most booked room
        int maxBooked = 0;
        for (int i = 1; i < n; i++) {
            if (bookingCounts[i] > bookingCounts[maxBooked]) {
                maxBooked = i;
            }
        }
        return maxBooked;
    }
}

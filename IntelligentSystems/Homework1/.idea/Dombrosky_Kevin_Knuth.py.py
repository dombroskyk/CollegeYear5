# Author: Kevin Dombrosky <kfd6490>
# Tests Knuth's conjecture on an elementary set of integers using BFS and a select few shortcuts and self limitations

from Queue import Queue
from math import factorial, sqrt, floor

def bfs(target):
    queue = Queue()
    queue.put((4, 0))
    curr = 4
    total_steps = 0
    seen = set()

    while True:
        if queue.empty():
            return "Queue empty, solution not found/possible"
        ordered_pair = queue.get()
        curr = ordered_pair[0]
        total_steps = ordered_pair[1]

        if curr in seen:
            continue
        seen.add(curr)

        is_int = True
        if isinstance(curr, float):
            is_int = curr.is_integer()

        if is_int:
            curr = int(curr)
        if curr == target:
            break

        try:
            if is_int and curr <= 197 and curr > 1:
                queue.put((factorial(curr), total_steps + 1))
        except (OverflowError):
            pass

        queue.put((sqrt(curr), total_steps+1))
        if not is_int and floor(curr) != 1:
            queue.put((floor(curr), total_steps + 1))

    return total_steps

print("5: {}".format(str(bfs(5))))
print("8: {}".format(str(bfs(8))))
print("10: {}".format(str(bfs(10))))
print("13: {}".format(str(bfs(13))))


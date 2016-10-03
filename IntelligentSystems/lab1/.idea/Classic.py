from PIL import Image
import heapq
import sys
import math

PATH = (255, 0, 0, 255)
FOOTPATH = (0, 0, 0, 255)
EASY_MOVEMENT_FOREST = (255, 255, 255, 255)
OPEN_LAND = (248, 148, 18, 255)
ROUGH_MEADOW = (255, 192, 0, 255)
SLOW_RUN_FOREST = (2, 208, 60, 255)
WALK_FOREST = (2, 136, 40, 255)
IMPASSABLE_VEGETATION = (5, 73, 24, 255)
WATER = (0, 0, 255, 255)
PAVED_ROAD = (71, 51, 3, 255)
OOB = (205, 0, 101, 255)

class Pixel(object):

    def __init__(self, color, elevation):
        self.color = color
        self.elevation = elevation
        self.path = False
        self.current_cost = float("inf")
        self.terrain_cost = None #TODO

    def travel_cost(self, target):
        pass

class OrienteeringMap(object):

    def __init__(self):
        self.__pixels = []

    def add_pixel(self, y, pixel):
        if y == len(self.__pixels):
            # init row of pixels for map
            self.__pixels.append([])
        self.__pixels[y].append(pixel)

    def show_map(self):
        show_img = Image.new("RGB", (len(self.__pixels[0]), len(self.__pixels)))
        # flatten list of pixels
        show_img.putdata([pixel.color for row in self.__pixels for pixel in row])
        show_img.show()

    def mark_pixel_seen(self, x, y):
        self.__pixels[y][x].path = True

    def prep_show_map(self):
        for row in self.__pixels:
            for pixel in row:
                if pixel.path:
                    pixel.color = PATH
        self.show_map()

    def get_pixel(self, x, y):
        return self.__pixels[y][x]

def main():
    # Get command line argument
    if len(sys.argv) < 2:
        print("Please provide a file name as the second command line argument.")
        exit(1)

    file_name = sys.argv[1]
    flags = []
    mode = "Classic"
    with open(file_name) as f:
        for i, line in enumerate(f):
            if i > 0:
                line = line.strip().split()
                flags.append((int(line[0]), int(line[1])))
            else:
                mode = line


    # build map image info
    terrain = Image.open("terrain.png")
    terrain_image_size = terrain.size
    pixel_access = terrain.load()

    # build elevation data
    elevations = []
    with open("mpp.txt") as elevation_txt:
        for line in elevation_txt:
            elevations.append( [float(x) for x in line.strip().split()] )

    map = OrienteeringMap()
    for y in range(terrain_image_size[1]):
        for x in range(terrain_image_size[0]):
            # elevation data offset
            elevation = elevations[y][x-5] if x > 4 else 0.0
            pixel = Pixel(pixel_access[x, y], elevation)
            map.add_pixel(y, pixel)

    #map.show_map()
    source_flag_index = 0
    for i in range(len(flags) - 1):
        if i == 0:
            map.get_pixel(flags[0][0], flags[0][1]).current_cost = 0
        parents = a_star(flags[i], flags[i+1], map)
        trace_map(flags[i+1], parents, map)

    map.prep_show_map()


def a_star(start, target, map):
    parents = {start: (0, "S")}
    pq = [(heuristic(start, target, map), start)]
    while len(pq) > 0:
        curr = heapq.heappop(pq)
        if curr[1] == target:
            return parents
        for s in successors(curr):
            if s not in parents:
                parents[s] = curr
                heapq.heappush(pq, (cost(s, target, parents, map), s))


def heuristic(source, target, map):
    pixel_color = map.get_pixel(source[0], source[1]).color
    if pixel_color == WATER or pixel_color == OOB or pixel_color == IMPASSABLE_VEGETATION:
        return float("inf")
    return abs(target[0] - source[0]) * 6.174 + abs(target[1] - source[1]) * 4.53

def successors(curr):
    curr_loc = curr[1]
    return [(curr_loc[0]+1, curr_loc[1]), (curr_loc[0]+1, curr_loc[1]-1), (curr_loc[0]+1, curr_loc[1]+1),
            (curr_loc[0]-1, curr_loc[1]), (curr_loc[0]-1, curr_loc[1]+1), (curr_loc[0]-1, curr_loc[1]-1),
            (curr_loc[0], curr_loc[1]+1), (curr_loc[0], curr_loc[1]-1)]


def cost(source, target, parents,  map):
    parent = parents[source][1]
    parent_pixel = map.get_pixel(parent[0], parent[1])
    source_cost = parent_pixel.current_cost
    cost_to_take = 0
    #print(str(parents))

    cost_to_take = abs(source[0] - parent[0]) * 6.174 + abs(source[1] - parent[1]) * 4.53
    source_pixel = map.get_pixel(source[0], source[1])
    pixel_color = source_pixel.color
    terrain_mult = 1
    if pixel_color == OPEN_LAND or pixel_color == PAVED_ROAD:
        terrain_mult = .5
    elif pixel_color == FOOTPATH:
        terrain_mult = .75
    elif pixel_color == EASY_MOVEMENT_FOREST:
        # not necessary, for completeness
        terrain_mult = 1
    elif pixel_color == SLOW_RUN_FOREST:
        terrain_mult = 1.5
    elif pixel_color == ROUGH_MEADOW or pixel_color == WALK_FOREST:
        terrain_mult = 2

    source_pixel.current_cost = source_cost + cost_to_take * terrain_mult

    # elevation change ratio per pixel distance
    elevation_ratio = parent_pixel.elevation - source_pixel.elevation / \
                        math.sqrt((source[0] - parent[0])**2 * 10.29 + (source[1] - parent[1])**2 * 7.29)
    elevation_mult = 1
    if elevation_ratio > 1 or elevation_ratio < -1:
        elevation_mult = 1.5
    elif elevation_ratio > .75:
        elevation_mult = 1.15
    elif elevation_ratio > .5:
        elevation_mult = 1.1
    elif elevation_ratio > .25:
        elevation_mult = 1.05
    elif elevation_ratio > 0:
        # not necessary, for completeness
        elevation_mult = 1
    elif elevation_ratio > -.25:
        elevation_mult = .95
    elif elevation_ratio > -.5:
        elevation_mult = .9
    elif elevation_ratio > -.75:
        elevation_mult = .85

    cost_to_take *= elevation_mult
    estimated_cost = heuristic(source, target, map)
    #return cost_target * height_multiplier
    print("source_cost: " + str(source_cost))
    print("cost_to_take: " + str(cost_to_take))
    print("estimated_cost: " + str(estimated_cost))
    return source_cost + cost_to_take + estimated_cost


def trace_map(end, parents, map):
    curr = end
    while curr != 'S':
        map.mark_pixel_seen(curr[0], curr[1])
        curr = parents[curr][1]


if __name__ == "__main__":
    main()
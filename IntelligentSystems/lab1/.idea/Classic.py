from PIL import Image
import heapq
import sys


class Pixel(object):

    def __init__(self, color, elevation):
        self.color = color
        self.elevation = elevation
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
                flags.append((line[0], line[1]))
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

    map.show_map()


def a_star(start):
    pq = [(heuristic(start), start)]
    parents = {start: "S"}
    while len(pq) > 0:
        curr = heapq.heappop(pq)
        if curr == target:
            return success
        for s in successors(curr):
            if s not in parents:
                heapq.heappush(pq, (heuristic(s), s))
                parents[s] = curr



def estimated_cost(source, target):
    distance = Math.sqrt( (target[1] - source[1])**2 + (target[0] - source[0])**2 )
    # return distance/2 * source_cost + distance/2 * avg_cost

def cost(source, destination):
    #height_ratio = (height_difference)/real_pixel_width
    #height_multiplier = mapping_switch
    #return cost_target * height_multiplier
    pass



if __name__ == "__main__":
    main()

ITERATIONS = 10

class WSMNode(object):

    def __init__(self, name, w_trans, s_trans, m_trans, init_prob, h_prob, n_prob, u_prob):
        self.name = name
        self.w_trans = w_trans
        self.s_trans = s_trans
        self.m_trans = m_trans
        self.state_prob = [init_prob]
        self.h_prob = h_prob
        self.n_prob = n_prob
        self.u_prob = u_prob


def main():
    working_node = WSMNode("Working", .5, .25, .25, .5, .25, .5, .25)
    surfing_node = WSMNode("Surfing", .5, .25, .25, .5, .5, 0, .5)
    meeting_node = WSMNode("Meeting", 1/3, 2/3, 0, 0, 0, 2/3, 1/3)

    nodes = [working_node, surfing_node, meeting_node]

    for i in range(ITERATIONS):
        for node in nodes:
            node.state_prob += [0]
        for node in nodes:
            meeting_node.state_prob[i+1] += node.state_prob[i] * node.m_trans
            surfing_node.state_prob[i+1] += node.state_prob[i] * node.s_trans
            working_node.state_prob[i+1] += node.state_prob[i] * node.w_trans

    print(working_node.state_prob)
    print(surfing_node.state_prob)
    print(meeting_node.state_prob)



    pass



if __name__ == "__main__":
    main()
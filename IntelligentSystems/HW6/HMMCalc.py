
# constant
ITERATIONS = 10


class WSMNode(object):

    def __init__(self, name, w_trans, s_trans, m_trans, init_prob, h_prob, n_prob, u_prob):
        self.name = name
        self.w_trans = w_trans
        self.s_trans = s_trans
        self.m_trans = m_trans
        self.state_prob = [init_prob]
        self.mood_prob = [init_prob * h_prob]
        self.h_prob = h_prob
        self.n_prob = n_prob
        self.u_prob = u_prob


def main():
    # init
    working_node = WSMNode("Working", .5, .25, .25, .5, .25, .5, .25)
    surfing_node = WSMNode("Surfing", .5, .25, .25, .5, .5, 0, .5)
    meeting_node = WSMNode("Meeting", 1/3, 2/3, 0, 0, 0, 2/3, 1/3)
    nodes = [working_node, surfing_node, meeting_node]

    # calc
    for i in range(ITERATIONS):
        for node in nodes:
            node.state_prob += [0]
            node.mood_prob += [0]
        for node in nodes:
            # calc state probs
            meeting_node.state_prob[i+1] += node.state_prob[i] * node.m_trans
            surfing_node.state_prob[i+1] += node.state_prob[i] * node.s_trans
            working_node.state_prob[i+1] += node.state_prob[i] * node.w_trans
            # calc mood probs
            if i % 2:
                meeting_node.mood_prob[i+1] += node.mood_prob[i] * node.m_trans * meeting_node.u_prob
                surfing_node.mood_prob[i+1] += node.mood_prob[i] * node.s_trans * surfing_node.u_prob
                working_node.mood_prob[i+1] += node.mood_prob[i] * node.w_trans * working_node.u_prob
            else:
                meeting_node.mood_prob[i+1] += node.mood_prob[i] * node.m_trans * meeting_node.h_prob
                surfing_node.mood_prob[i+1] += node.mood_prob[i] * node.s_trans * surfing_node.h_prob
                working_node.mood_prob[i+1] += node.mood_prob[i] * node.w_trans * working_node.h_prob

    # output
    print("State Probability")
    print(repr("Time"), repr("Meeting").rjust(8), repr("Surfing").rjust(8), repr("Working").rjust(8))
    for i in range(ITERATIONS+1):
        print("t"+str(i)+"      {0:5f} {1:5f} {2:5f}".format(meeting_node.state_prob[i], surfing_node.state_prob[i],
                                                             working_node.state_prob[i]))

    print()
    print("State-Mood probability")
    print(repr("Time"), repr(" Meeting").rjust(8), repr(" Surfing").rjust(8), repr(" Working").rjust(8))
    for i in range(ITERATIONS+1):
        print("t" + str(i) + "      {0:6f} {1:6f} {2:6f}".format(meeting_node.mood_prob[i], surfing_node.mood_prob[i],
                                                                 working_node.mood_prob[i]))


if __name__ == "__main__":
    main()

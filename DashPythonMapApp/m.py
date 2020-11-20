from main.algorithms.shortest_path import Dijkstra
from main.graph.network import Network

def read_network_from_file(file_name, delimeter=','):
    """ Read from a file and build a network
    file_name: file to read from
    delimeter: delimeter that separates fields
    """
    cities = list()
    distances = dict()

    f = open(file_name, 'r')
    f = f.readlines()
    firstLine = f.pop(0)
    for line in f:
        #print(line)
        fields = line.rstrip().split(delimeter)
        city_1 = fields[11].strip("")
        city_2 = fields[16].strip("")
        distance = float(fields[24])

        # build the list of cities
        if city_1 not in cities:
            cities.append(city_1)
        if city_2 not in cities:
            cities.append(city_2)

        # build the dictionary based on city distances
        if cities.index(city_1) not in distances.keys():
            distances[cities.index(city_1)] = {cities.index(city_2): distance}
        if cities.index(city_2) not in distances[cities.index(city_1)].keys():
            distances[cities.index(city_1)][cities.index(city_2)] = distance
    return cities, distances

import csv
def main(fro, to):
    # application salutation
    application_name = 'Network Analysis'
    print('-' * len(application_name))
    print(application_name)
    print('-' * len(application_name))

    # read network from file
    file_name = 'finaldata.csv'
    cities, distances = read_network_from_file(file_name)

    # build the network
    network = Network()
    network.add_nodes(cities)
    for connection in distances.items():
        frm = cities[connection[0]]
        for connection_to in connection[1].items():
            network.add_edge(frm, cities[connection_to[0]], connection_to[1])

    # uncomment to print the network
    # print(network)

    # get from user the start city
    cityindex={}
    for (index, city) in enumerate(network.get_nodes()):
        cityindex[index]=city

    field_names = ['Index', 'City']
    #save city index
    '''
    with open('dict.csv', 'w') as csv_file:
        writer = csv.writer(csv_file)
        for key, value in cityindex.items():
            writer.writerow([key, value])'''

    start_city_index = fro
    start_city = network.get_nodes()[int(start_city_index)]

    # get from user the end city
    end_city_index = to
    end_city = network.get_nodes()[int(end_city_index)]

    # using Dijkstra's algorithm, compute smallest distance from start to end city
    Dijkstra.compute(network, network.get_node(start_city))

    for target_node in network.get_nodes():
        target_city = network.get_node(target_node)
        path = [target_city.get_name()]
        Dijkstra.compute_shortest_path(target_city, path)

        if path[0] == end_city:
            start_end_path = path[::-1]
            distance = target_city.get_weight()
    #print(f'{start_city} -> {end_city} = {start_end_path} : {distance}')
    return start_end_path

if __name__ == '__main__':
    main(10, 50)

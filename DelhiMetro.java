
//CSCM12J coursework
//name: Raihanath Changinim Kunnath Muhammedkutty
//student id: 2154457

import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

class Station {
    // class Station
    boolean visited = false;
    String _name;
    ArrayList<Line> _lines = new ArrayList<Line>();

    Station(String n) {
        _name = n;
    }

    // accessors
    public String name() {
        return _name;
    }

    public ArrayList<Line> lines() {
        return _lines;
    }

    public void setVisited() {
        visited = true;
    }

    public void setUnvisited() {
        visited = false;
    }

    public boolean hasBeenVisited() {
        return visited;
    }

    public ArrayList<Station> neighbourStations() {
        // code to be completed
        ArrayList<Station> nStations = new ArrayList<Station>();
        for (Line l : lines()) {

            for (Station s : l.stations()) {

                if (s.name().equals(name())) {

                    int pos = l.index(s);

                    if (pos > 0) {

                        nStations.add(l.stations.get(pos - 1));
                    } else if (pos != (l.stations().size() - 1)) {

                        nStations.add(l.stations.get(pos + 1));
                    }
                }
            }
        }

        return nStations;
    }
}

class Line {
    // class Line
    String _name;
    ArrayList<Station> stations = new ArrayList<Station>();
    HashMap<Station, Integer> stationMap = new HashMap<Station, Integer>(); // needed to quickly access the index of the
    // station in the ArrayList

    Line(String name) {
        this._name = new String(name);
    }

    // accessors
    public String name() {
        return _name;
    }

    public ArrayList<Station> stations() {
        return stations;
    }

    public int index(Station s) {
        // code to be completed
        return stationMap.get(s);
    }

    public void addStation(Station s) {
        // code to be completed
        stations.add(s);
        stationMap.put(s, (stations.size() - 1));
    }

}

class Metro {
    // class Metro
    ArrayList<Line> _lines = new ArrayList<Line>();
    ArrayList<Station> _stations = new ArrayList<Station>();

    // accessors
    ArrayList<Line> lines() {
        return _lines;
    }

    public ArrayList<Station> stations() {
        return _stations;
    }

    public HashMap<String, Station> stationMap = new HashMap<String, Station>(); // needed to quickly access the index
    // of the station in the ArrayList

    public static void printStationList(String message, ArrayList<Station> slist) {
        System.out.println(message);
        for (Station s : slist)
            System.out.println("=>" + s.name());
        System.out.println("\t(" + slist.size() + " stations)\n");
    }

    public static void printStationListWithLines(String message, ArrayList<Station> slist) {
        System.out.println(message);
        for (Station s : slist) {
            System.out.print("=>" + s.name() + "\t\t");
            for (Line l : s.lines())
                System.out.print("\t*" + l.name());
            System.out.println();
        }
        System.out.println("\t(" + slist.size() + " stations)\n");
    }

    public Station getStation(String name) {
        return stationMap.get(name);
    }

    // this function getAllConnectionHubs is to be completed. It shall returns the
    // list of all the stations that are a hub.
    public ArrayList<Station> getAllConnectionHubs() {
        // code to be completed
        ArrayList<Station> stationsHub = new ArrayList<Station>();
        for (Station st : stations()) {

            if (st.lines().size() > 1) {

                stationsHub.add(st);
            }
        }

        return stationsHub;

    }

    public ArrayList<Station> shortestPath(String a, String b) {
        // code to be completed
        //shortest path can be calculated by Dijkstra Algorithm

        ArrayList<ArrayList<Station>> pathList = allPaths(a, b);

        int pos = 0;
        int stationCount = 0;

        for(int i=0; i<pathList.size(); i++) {

            if(i==0) {

                pos=0;
                stationCount = pathList.get(i).size();
            }else {
                if(pathList.get(i).size()<stationCount) {

                    pos=i;
                    stationCount = pathList.get(i).size();
                }

            }
        }

        return pathList.get(pos);
    }

    public ArrayList<ArrayList<Station>> allPaths(String a, String b) {
        // code to be completed
        //All paths can be calculated by Depth First Search Algorithm

        Station src = stationMap.get(a);
        Station dest = stationMap.get(b);
        ArrayList<Station> nStations = src.neighbourStations();
        ArrayList<ArrayList<Station>> pathsList = new ArrayList<>();
        ArrayList<ArrayList<Station>> pathsListF = new ArrayList<>();
        ArrayList<Station> currentNstations = new ArrayList<>();
        HashMap<Station, Station> nStationsMap = new HashMap<>();
        Station currentStation = src;
        int count = 0;

        while(count<=227) {

            for (int i = 0; i < nStations.size(); i++) {

                if (!nStations.get(i).hasBeenVisited()) {
                    ArrayList<Station> path = new ArrayList<>();

                    if(count==0) {
                        path.add(currentStation);
                        path.add(nStations.get(i));
                        pathsList.add(path);
                        currentStation = nStations.get(i);
                    }else {

                        for(int j = 0; j < pathsList.size(); j++) {

                            currentStation = nStationsMap.get(nStations.get(i));
                            path = pathsList.get(j);
                            if(path.get(path.size()-1).equals(currentStation)) {

                                path.add(nStations.get(i));
                                pathsList.add(path);
                            }
                        }
                    }

                    ArrayList<Station> cnStations = nStations.get(i).neighbourStations();
                    for (int z=0; z<cnStations.size(); z++) {

                        if(!cnStations.get(z).equals(dest)) {
                            nStationsMap.put(cnStations.get(z), currentStation);
                            currentNstations.add(cnStations.get(z));
                        }
                    }

                }
            }

            count++;
            currentStation.setVisited();
            nStations.addAll(currentNstations);
            currentNstations.clear();

        }

        for(int x=0; x<pathsList.size(); x++) {

            if(pathsList.get(x).get(pathsList.get(x).size()-1).equals(dest)) {

                pathsListF.add(pathsList.get(x));
            }
        }

        return pathsListF;
    }

    void readLines(String txt) {
        // reading the metro lines
        try (BufferedReader br = new BufferedReader(new FileReader(txt))) {
            String line;
            Line currentLine = null;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0)
                    if (line.contains(":")) { // We have a new line
                        currentLine = new Line(line.replace(":", ""));
                        lines().add(currentLine);
                    } else {
                        // we have a station
                        Station s = stationMap.get(line);
                        if (s == null) {
                            // station is new
                            s = new Station(line);
                            _stations.add(s);
                            stationMap.put(line, s); // Adding a station into the hashmap so that it can be retrieved
                            // quickly.
                        }
                        s.lines().add(currentLine);
                        currentLine.addStation(s);
                    }
                // process the line.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    static Metro delhi = new Metro();

    public static void main(String[] args) {
        delhi.readLines("lines.txt");
        System.out.println("number of stations:" + delhi.stations().size());
        Metro.printStationListWithLines("\nList of hubs:\n", delhi.getAllConnectionHubs());

        if (args.length == 2) {
            Metro.printStationList("\nShortest Path Stations: " + args[0] + " => " + args[1] + "\n",
                    delhi.shortestPath(args[0], args[1]));
            long startTime = System.currentTimeMillis();
            ArrayList<ArrayList<Station>> res = delhi.allPaths(args[0], args[1]);
            long endTime = System.currentTimeMillis();
            System.out.println("\n Number of different paths found between " + args[0] + " and " + args[1] + ":"
                    + res.size() + "\n");
            System.out.println("Calculation took " + (endTime - startTime) + " ms!");
        }
    }
}

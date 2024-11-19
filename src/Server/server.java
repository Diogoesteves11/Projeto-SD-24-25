package server;

import java.util.ArrayList;
import java.util.List;

import utils.Database;
import utils.Package;

public class Server{
    private final Database database;
    private final int maxClients;
    private final List<Package> SimplePackageList;

    public Server(int maxClients){
        this.database = new Database();
        this.maxClients = maxClients;
        this.SimplePackageList = new ArrayList<>();
    }





}
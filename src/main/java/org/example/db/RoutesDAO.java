package org.example.db;

import org.example.models.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesDAO {
    public List<Route> getAllRoutes () {
        Map<Integer, Route> routeMap = new HashMap<>();

        return new ArrayList<>(routeMap.values());
    }
}

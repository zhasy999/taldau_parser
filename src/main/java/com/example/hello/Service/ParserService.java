package com.example.hello.Service;
import com.example.hello.Repo.*;
import com.example.hello.Entity.*;
import com.example.hello.Formats.DataFormatter;
import com.example.hello.Repo.Sewerage_repo;
import com.example.hello.Repo.Sewerage_need_repair_repo;
import com.example.hello.Repo.Water_pipes_repo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ParserService {

    private static final Logger LOG = LoggerFactory.getLogger(ParserService.class);

    private final Counter_house_repo counter_house_repo;
    private final Counter_house_remote_repo counter_house_remote_repo;
    private final Counter_personal_repo counter_personal_repo;
    private final Counter_personal_remote_repo counter_personal_remote_repo;
    private final Sewerage_repo sewerage_repo;
    private final Sewerage_need_repair_repo sewerage_need_repair_repo;
    private final Water_pipes_repo water_pipes_repo;
    private final Water_pipes_construction_repo water_pipes_construction_repo;
    private final Water_pipes_construction_crash_repo water_pipes_construction_crash_repo;
    private final Water_pipes_crash_repo water_pipes_crash_repo;
    private final Water_pipes_need_repair_repo water_pipes_need_repair_repo;
    private final Water_pipes_net_repo water_pipes_net_repo;

    public ParserService(Counter_house_repo counter_house_repo, Counter_house_remote_repo counter_house_remote_repo, Counter_personal_repo counter_personal_repo, Counter_personal_remote_repo counter_personal_remote_repo, Sewerage_repo sewerage_repo, Sewerage_need_repair_repo sewerage_need_repair_repo, Water_pipes_repo water_pipes_repo, Water_pipes_construction_repo water_pipes_construction_repo, Water_pipes_construction_crash_repo water_pipes_construction_crash_repo, Water_pipes_crash_repo water_pipes_crash_repo, Water_pipes_need_repair_repo water_pipes_need_repair_repo, Water_pipes_net_repo water_pipes_net_repo) {
        this.counter_house_repo = counter_house_repo;
        this.counter_house_remote_repo = counter_house_remote_repo;
        this.counter_personal_repo = counter_personal_repo;
        this.counter_personal_remote_repo = counter_personal_remote_repo;
        this.sewerage_repo = sewerage_repo;
        this.sewerage_need_repair_repo = sewerage_need_repair_repo;
        this.water_pipes_repo = water_pipes_repo;
        this.water_pipes_construction_repo = water_pipes_construction_repo;
        this.water_pipes_construction_crash_repo = water_pipes_construction_crash_repo;
        this.water_pipes_crash_repo = water_pipes_crash_repo;
        this.water_pipes_need_repair_repo = water_pipes_need_repair_repo;
        this.water_pipes_net_repo = water_pipes_net_repo;
    }

    public ArrayList<ArrayList<Integer>> locationList(){
        ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>(15);
        rows.add(new ArrayList<>(Arrays.asList(256620, 741917, 808342)));
        rows.add(new ArrayList<>(Arrays.asList(256627,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(256636,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(256694,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(256709,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(256713,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(256721,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(256779,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(257147,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(257257,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(257376,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(257880,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(257993,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(258074,741917,808342)));
        rows.add(new ArrayList<>(Arrays.asList(258445,741917,808342)));
        return rows;
    }

    public String getUrl(String a, String b){
        String url = "https://taldau.stat.gov.kz/ru/Api/GetIndexData/"+a+"?period=7&dics="+b;
        return url;
    }

    public String conn(String url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        String response;

        try (InputStream is = connection.getInputStream()) {
            response = org.apache.commons.io.IOUtils.toString(is, StandardCharsets.UTF_8);
            return response;
        }
        catch(IOException ex){
            System.out.println (ex.toString());
            return "Error";
        }
    }

    public void counter_house() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("counter_house");
        LOG.info(m, "counter_house - start");
        String url = getUrl("701757","68,776");
        LOG.info(m, "url - {}", url);

        List<Counter_house> counterHouseList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1))) {
                    System.out.println(r.get(0));
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Counter_house counter_house = new Counter_house();
                            counter_house.setRegion(f.getTermName().get(0));
                            counter_house.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            counter_house.setCounter(Long.parseLong(p.getValue()));
                            counterHouseList.add(counter_house);
                        }
                    }
                }
            }
        }
        counter_house_repo.deleteAll();
        counter_house_repo.saveAll(counterHouseList);
        LOG.info(m, "counter_house - end");
    }

    public void counter_house_remote() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("counter_house_remote");
        LOG.info(m, "counter_house_remote - start");
        String url = getUrl("20237329","68,776");
        LOG.info(m, "url - {}", url);

        List<Counter_house_remote> counterHouseRemoteList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Counter_house_remote counter_house_remote = new Counter_house_remote();
                            counter_house_remote.setRegion(f.getTermName().get(0));
                            counter_house_remote.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            counter_house_remote.setCounter(Long.parseLong(p.getValue()));
                            counterHouseRemoteList.add(counter_house_remote);
                        }
                    }

                }
            }
        }
        counter_house_remote_repo.deleteAll();
        counter_house_remote_repo.saveAll(counterHouseRemoteList);
        LOG.info(m, "counter_house_remote - end");
    }


    public void counter_personal() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("counter_personal");
        LOG.info(m, "counter_personal- start");
        String url = getUrl("16166959","68,776");
        LOG.info(m, "url - {}", url);

        List<Counter_personal> counterPersonalList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Counter_personal counter_personal = new Counter_personal();
                            counter_personal.setRegion(f.getTermName().get(0));
                            counter_personal.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            counter_personal.setCounter(Long.parseLong(p.getValue()));
                            counterPersonalList.add(counter_personal);
                        }
                    }
                }
            }
        }
        counter_personal_repo.deleteAll();
        counter_personal_repo.saveAll(counterPersonalList);
        LOG.info(m, "counter_personal - end");
    }

    public void counter_personal_remote() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("counter_personal_remote");
        LOG.info(m, "counter_personal_remote- start");
        String url = getUrl("16166959","68,776");
        LOG.info(m, "url - {}", url);

        List<Counter_personal_remote> counterPersonalRemoteList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Counter_personal_remote counter_personal_remote = new Counter_personal_remote();
                            counter_personal_remote.setRegion(f.getTermName().get(0));
                            counter_personal_remote.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            counter_personal_remote.setCounter(Long.parseLong(p.getValue()));
                            counterPersonalRemoteList.add(counter_personal_remote);
                        }
                    }

                }
            }
        }
        counter_personal_remote_repo.deleteAll();
        counter_personal_remote_repo.saveAll(counterPersonalRemoteList);
        LOG.info(m, "counter_personal_remote - end");
    }

    public void sewerage() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("sewerage");
        LOG.info(m, "sewerage - start");
        String url = getUrl("701787","68,776,317");
        LOG.info(m, "url - {}", url);

        List<Sewerage> sewerageList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1)) && f.getTerms().get(2).equals(r.get(2))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Sewerage sewerage = new Sewerage();
                            sewerage.setRegion(f.getTermName().get(0));
                            sewerage.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            sewerage.setSewerage(Long.parseLong(p.getValue()));
                            sewerageList.add(sewerage);
                        }
                    }

                }
            }
        }
        sewerage_repo.deleteAll();
        sewerage_repo.saveAll(sewerageList);
        LOG.info(m, "sewerage - end");
    }

    public void sewerage_need_repair() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("sewerage_need_repair");
        LOG.info(m, "sewerage_need_repair - start");
        String url = getUrl("701788","68,776,317");
        LOG.info(m, "url - {}", url);

        List<Sewerage_need_repair> sewerage_need_repairList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1)) && f.getTerms().get(2).equals(r.get(2))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Sewerage_need_repair sewerage_need_repair = new Sewerage_need_repair();
                            sewerage_need_repair.setRegion(f.getTermName().get(0));
                            sewerage_need_repair.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            sewerage_need_repair.setSewerage(Long.parseLong(p.getValue()));
                            sewerage_need_repairList.add(sewerage_need_repair);
                        }
                    }
                }
            }
        }
        sewerage_need_repair_repo.deleteAll();
        sewerage_need_repair_repo.saveAll(sewerage_need_repairList);
        LOG.info(m, "sewerage_need_repair - end");
    }

    public void water_pipes() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("water_pipes");
        LOG.info(m, "water_pipes - start");
        String url = getUrl("701785","68,776,317");
        LOG.info(m, "url - {}", url);

        List<Water_pipes> water_pipesList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1)) && f.getTerms().get(2).equals(r.get(2))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Water_pipes water_pipes = new Water_pipes();
                            water_pipes.setRegion(f.getTermName().get(0));
                            water_pipes.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            water_pipes.setWater_pipes(Long.parseLong(p.getValue()));
                            water_pipesList.add(water_pipes);
                        }
                    }
                }
            }
        }
        water_pipes_repo.deleteAll();
        water_pipes_repo.saveAll(water_pipesList);
        LOG.info(m, "water_pipes - end");
    }

    public void water_pipes_construction() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("water_pipes_construction");
        LOG.info(m, "water_pipes_construction - start");
        String url = getUrl("701751","68,776");
        LOG.info(m, "url - {}", url);

        List<Water_pipes_construction> water_pipes_constructionList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Water_pipes_construction water_pipes_construction = new Water_pipes_construction();
                            water_pipes_construction.setRegion(f.getTermName().get(0));
                            water_pipes_construction.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            water_pipes_construction.setConstruction(Long.parseLong(p.getValue()));
                            water_pipes_constructionList.add(water_pipes_construction);
                        }
                    }
                }
            }
        }
        water_pipes_construction_repo.deleteAll();
        water_pipes_construction_repo.saveAll(water_pipes_constructionList);
        LOG.info(m, "water_pipes_construction - end");
    }

    public void water_pipes_construction_crash() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("water_pipes_construction_crash");
        LOG.info(m, "water_pipes_construction_crash - start");
        String url = getUrl("701755","68,776");
        LOG.info(m, "url - {}", url);

        List<Water_pipes_construction_crash> water_pipes_construction_crashList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Water_pipes_construction_crash water_pipes_construction_crash = new Water_pipes_construction_crash();
                            water_pipes_construction_crash.setRegion(f.getTermName().get(0));
                            water_pipes_construction_crash.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            water_pipes_construction_crash.setCrash(Long.parseLong(p.getValue()));
                            water_pipes_construction_crashList.add(water_pipes_construction_crash);
                        }
                    }
                }
            }
        }
        water_pipes_construction_crash_repo.deleteAll();
        water_pipes_construction_crash_repo.saveAll(water_pipes_construction_crashList);
        LOG.info(m, "water_pipes_construction_crash - end");
    }

    public void water_pipes_crash() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("water_pipes_crash");
        LOG.info(m, "water_pipes_crash - start");
        String url = getUrl("701756","67,749");
        LOG.info(m, "url - {}", url);

        List<Water_pipes_crash> water_pipes_crashList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Water_pipes_crash water_pipes_crash = new Water_pipes_crash();
                            water_pipes_crash.setRegion(f.getTermName().get(0));
                            water_pipes_crash.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            water_pipes_crash.setCrash(Long.parseLong(p.getValue()));
                            water_pipes_crashList.add(water_pipes_crash);
                        }
                    }
                }
            }
        }
        water_pipes_crash_repo.deleteAll();
        water_pipes_crash_repo.saveAll(water_pipes_crashList);
        LOG.info(m, "water_pipes_crash - end");
    }

    public void water_pipes_need_repair() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("water_pipes_need_repair");
        LOG.info(m, "water_pipes_need_repair - start");
        String url = getUrl("701786","68,776,317");
        LOG.info(m, "url - {}", url);

        List<Water_pipes_need_repair> water_pipes_need_repairList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1)) && f.getTerms().get(2).equals(r.get(2))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Water_pipes_need_repair water_pipes_need_repair = new Water_pipes_need_repair();
                            water_pipes_need_repair.setRegion(f.getTermName().get(0));
                            water_pipes_need_repair.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            water_pipes_need_repair.setWater_pipes(Long.parseLong(p.getValue()));
                            water_pipes_need_repairList.add(water_pipes_need_repair);
                        }
                    }
                }
            }
        }
        water_pipes_need_repair_repo.deleteAll();
        water_pipes_need_repair_repo.saveAll(water_pipes_need_repairList);
        LOG.info(m, "water_pipes_need_repair - end");
    }

    public void water_pipes_net() throws ParseException, IOException {
        Marker m = MarkerFactory.getMarker("water_pipes_net");
        LOG.info(m, "water_pipes_net - start");
        String url = getUrl("701752","68,776");
        LOG.info(m, "url - {}", url);

        List<Water_pipes_net> water_pipes_netList = new ArrayList<>();
        List<DataFormatter.Full> fullList = DataFormatter.fromJsonString(conn(url));
        LOG.info(m, "count - {}", fullList.size());
        ArrayList<ArrayList<Integer>> regions = locationList();
        for (DataFormatter.Full f : fullList) {
            for ( ArrayList<Integer> r : regions) {
                if (f.getTerms().get(0).equals(r.get(0)) && f.getTerms().get(1).equals(r.get(1))) {
                    List<DataFormatter.Periods> periodList = f.getPeriods();
                    for (DataFormatter.Periods p : periodList) {
                        if (!p.getValue().equalsIgnoreCase("x")) {
                            Water_pipes_net water_pipes_net = new Water_pipes_net();
                            water_pipes_net.setRegion(f.getTermName().get(0));
                            water_pipes_net.setYear(Long.parseLong(p.getDate().substring(p.getDate().length() - 4)));
                            water_pipes_net.setNet(Long.parseLong(p.getValue()));
                            water_pipes_netList.add(water_pipes_net);
                        }
                    }
                }
            }
        }
        water_pipes_net_repo.deleteAll();
        water_pipes_net_repo.saveAll(water_pipes_netList);
        LOG.info(m, "water_pipes_net - end");
    }
}



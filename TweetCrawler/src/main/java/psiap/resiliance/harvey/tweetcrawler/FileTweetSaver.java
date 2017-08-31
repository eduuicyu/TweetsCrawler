/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psiap.resiliance.harvey.tweetcrawler;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.logging.Logger;
import twitter4j.Status;

/**
 *
 * @author ubuntu
 */
public class FileTweetSaver implements Consumer<Status>, AutoCloseable {

//    private static final Logger LOG = Logger.getLogger(FileTweetSaver.class.getName());
    private static final Gson GSON = new Gson();
    private final PrintStream ps;

    public FileTweetSaver(String fileName) throws FileNotFoundException {
        ps = new PrintStream(fileName);
    }

    @Override
    public void accept(Status t) {
        ps.println(GSON.toJson(t));
        ps.flush();
    }

    @Override
    public void close() throws IOException {
        ps.flush();
        ps.close();
    }

}

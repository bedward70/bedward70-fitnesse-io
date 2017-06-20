package ru.bedward70.fitnesse.io;

import fitnesse.junit.FitNesseRunner;
import fitnesseMain.FitNesseMain;

/**
 * Created by ebalovnev on 20.06.17.
 */
public class B70FitNesseRunner extends FitNesseRunner {

    public B70FitNesseRunner(Class<?> suiteClass) throws Exception {
        super(suiteClass);
        FitNesseMain.main(new String[] {"-i"});
    }
}

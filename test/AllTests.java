package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite kerho-ohjelmalle
 * @author vesal
 * @author Johanna Virkkunen
 * @version 3.1.2019
 * @version 6.5.2019
 */
@RunWith(Suite.class)
@SuiteClasses({
    kanta.HetuTarkistusTest.class,
    kanta.SisaltaaTarkistajaTest.class,
    kanta.TietueTest.class,
    mainonta.HenkiloTest.class,
    mainonta.HenkilotTest.class,
    mainonta.MielenkiintoTest.class,
    mainonta.MielenkiinnotTest.class,
    mainonta.MainontaTest.class
    })
public class AllTests {
 //
}
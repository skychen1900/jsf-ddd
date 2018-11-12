package org.vermeerlab;

import java.util.Collection;
import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

//@Ignore
public class CycleTest {

    @Rule
    public ErrorCollector ec = new ErrorCollector();

    private JDepend jdepend;

    @Before
    public void setUp() throws Exception {
        jdepend = new JDepend();
        // クラスファイルが出力されるディレクトリを指定
        jdepend.addDirectory("target/classes");
    }

    @Test
    public void 全てのパッケージが循環依存を持たない() throws Exception {
        jdepend.analyze();
        assertThat(jdepend.containsCycles(), is(false));
    }

    @Test
    public void 全てのパッケージが循環依存を持たない_パッケージ名出力()
            throws Exception {
        @SuppressWarnings("unchecked")
        final Collection<JavaPackage> packages = jdepend.analyze();
        for (final JavaPackage p : packages) {
            ec.checkThat(
                    "循環依存しているパッケージ名 : " + p.getName(),
                    p.containsCycle(), is(false));
        }
    }
}

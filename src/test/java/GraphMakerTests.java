import edu.byu.mse.graph.Node;
import edu.byu.mse.graph.URLGraph;
import edu.byu.mse.graph.URLNode;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("GraphMaker Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GraphMakerTests {

    private URLGraph graph;
    private URLNode head;
    private URLNode child;
    @BeforeAll
    void setup() {
        head = new URLNode("root");
        head.getNodeData().getData().put("changefreq", "daily");

        URLNode child1 = new URLNode("advisement");
        child1.getNodeData().getData().put("changefreq", "weekly");
        URLNode child2 = new URLNode("deans");
        child2.getNodeData().getData().put("changefreq", "weekly");
        URLNode child3 = new URLNode("employee_portal");
        child3.getNodeData().getData().put("changefreq", "monthly");
        URLNode child2_5 = new URLNode("deans/financial-aid");
        child2_5.getNodeData().getData().put("changefreq", "yearly");
        child2_5.setParent(child2);
        child2.addChildren(child2_5);

        head.addChildren(child1);
        head.addChildren(child2);
        head.addChildren(child3);

        child = child2;
    }

    @Test
    @DisplayName("Test readObject function")
    @Tag("GraphMaker")
    void testReadObjects() {
        graph = new URLGraph(head);

        List<Node> nodes = graph.readObjects();

        assertAll(
                () -> assertEquals(5, nodes.size()),
                () -> assertTrue(doesContain("deans/financial-aid", nodes)),
                () -> assertTrue(doesContain("employee_portal", nodes)),
                () -> assertTrue(doesContain("deans", nodes)),
                () -> assertTrue(doesContain("advisement", nodes)),
                () -> assertTrue(doesContain("root", nodes))
        );
    }

    @Test
    @DisplayName("Test readObject function but given a child")
    @Tag("GraphMaker")
    void testReadObjectsChildAsHead() {
        graph = new URLGraph(child);

        List<Node> nodes = graph.readObjects();

        assertAll(
                () -> assertEquals(2, nodes.size()),
                () -> assertTrue(doesContain("deans/financial-aid", nodes)),
                () -> assertFalse(doesContain("employee_portal", nodes)),
                () -> assertTrue(doesContain("deans", nodes)),
                () -> assertFalse(doesContain("advisement", nodes)),
                () -> assertFalse(doesContain("root", nodes))
        );
    }

    @Test
    @DisplayName("Test importObject function")
    @Tag("GraphMaker")
    void testImportObjects() {
        String[] headers = {"loc", "param1", "param2"};
        String[][] values = {
                {"https://education.byu.edu/", "hello1", "world1"},
                {"https://education.byu.edu/deans/financial-aid", "hello", "world"},
                {"https://education.byu.edu/employee_portal", "hello2", "world2"},
                {"https://education.byu.edu/deans", "hello3", "world3"},
                {"https://education.byu.edu/advisement", "hello4", "world4"},
                {"https://education.byu.edu/advisement/overview/directory", "hello5", "world5"},
        };

        graph = new URLGraph();

        graph.setHeaders(headers);

        assertDoesNotThrow(() -> {
            for(String[] arr : values)
                graph.importObject(arr);
        });

        List<Node> nodes = graph.readObjects();

        assertAll(
                () -> assertEquals(6, nodes.size()),
                () -> assertTrue(doesContain("root", nodes)),
                () -> assertTrue(doesContain("deans/financial-aid", nodes)),
                () -> assertTrue(doesContain("employee_portal", nodes)),
                () -> assertTrue(doesContain("deans", nodes)),
                () -> assertTrue(doesContain("advisement", nodes)),
                () -> assertTrue(doesContain("advisement/overview/directory", nodes))
        );
    }

    @Test
    @DisplayName("Test importObject function but delayed parent insert")
    @Tag("GraphMaker")
    void testImportObjectsParentDelayed() {
        String[] headers = {"loc", "param1", "param2"};
        String[][] values = {
                {"https://education.byu.edu/", "hello1", "world1"},
                {"https://education.byu.edu/advisement/overview", "hello", "world"},
                {"https://education.byu.edu/advisement", "hello2", "world2"}
        };

        graph = new URLGraph();

        graph.setHeaders(headers);

        assertDoesNotThrow(() -> {
            for(String[] arr : values)
                graph.importObject(arr);
        });

        List<Node> nodes = graph.readObjects();

        assertAll(
                () -> assertEquals(3, nodes.size()),
                () -> assertTrue(doesContain("root", nodes)),
                () -> assertTrue(doesContain("advisement", nodes)),
                () -> assertTrue(doesContain("advisement/overview", nodes))
        );
    }



    private boolean doesContain(String url, List<Node> nodes) {
        for(Node node : nodes)
            if(node.getValue().equals(url))
                return true;

        return false;
    }
}

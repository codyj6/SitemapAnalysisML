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
        head = new URLNode("root", "daily");

        URLNode child1 = new URLNode("advisement", "daily");
        URLNode child2 = new URLNode("deans", "weekly");
        URLNode child3 = new URLNode("employee_portal", "weekly");
        URLNode child2_5 = new URLNode("deans/financial-aid", "weekly");
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
        NodeList nList = getMockedNodeList();

        graph = new URLGraph(nList);

        assertDoesNotThrow(() -> {
            graph.importObjects();
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
        NodeList nList = getMockedNodeList2();

        graph = new URLGraph(nList);

        assertDoesNotThrow(() -> {
            graph.importObjects();
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

    private NodeList getMockedNodeList2() {
        NodeList nList = mock(NodeList.class);
        Element elem1 = mock(Element.class);
        Element elem2 = mock(Element.class);
        Element elem3 = mock(Element.class);

        Mockito.when(elem1.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);
        Mockito.when(elem2.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);
        Mockito.when(elem3.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);

        NodeList elem1Loc = mockNodeList("https://education.byu.edu/");
        NodeList elem1Change = mockNodeList("daily");

        Mockito.when(elem1.getElementsByTagName("loc")).thenReturn(elem1Loc);
        Mockito.when(elem1.getElementsByTagName("changefreq")).thenReturn(elem1Change);

        NodeList elem2Loc = mockNodeList("https://education.byu.edu/advisement/overview");
        NodeList elem2Change = mockNodeList("daily");

        Mockito.when(elem2.getElementsByTagName("loc")).thenReturn(elem2Loc);
        Mockito.when(elem2.getElementsByTagName("changefreq")).thenReturn(elem2Change);

        NodeList elem3Loc = mockNodeList("https://education.byu.edu/advisement");
        NodeList elem3Change = mockNodeList("weekly");

        Mockito.when(elem3.getElementsByTagName("loc")).thenReturn(elem3Loc);
        Mockito.when(elem3.getElementsByTagName("changefreq")).thenReturn(elem3Change);

        Mockito.when(nList.getLength()).thenReturn(3);
        Mockito.when(nList.item(0)).thenReturn(elem1);
        Mockito.when(nList.item(1)).thenReturn(elem2);
        Mockito.when(nList.item(2)).thenReturn(elem3);

        return nList;
    }

    private NodeList getMockedNodeList() {
        NodeList nList = mock(NodeList.class);
        Element elem1 = mock(Element.class);
        Element elem2 = mock(Element.class);
        Element elem3 = mock(Element.class);
        Element elem4 = mock(Element.class);
        Element elem5 = mock(Element.class);
        Element elem6 = mock(Element.class);

        Mockito.when(elem1.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);
        Mockito.when(elem2.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);
        Mockito.when(elem3.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);
        Mockito.when(elem4.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);
        Mockito.when(elem5.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);
        Mockito.when(elem6.getNodeType()).thenReturn(org.w3c.dom.Node.ELEMENT_NODE);

        NodeList elem1Loc = mockNodeList("https://education.byu.edu/");
        NodeList elem1Change = mockNodeList("daily");

        Mockito.when(elem1.getElementsByTagName("loc")).thenReturn(elem1Loc);
        Mockito.when(elem1.getElementsByTagName("changefreq")).thenReturn(elem1Change);

        NodeList elem2Loc = mockNodeList("https://education.byu.edu/advisement");
        NodeList elem2Change = mockNodeList("daily");

        Mockito.when(elem2.getElementsByTagName("loc")).thenReturn(elem2Loc);
        Mockito.when(elem2.getElementsByTagName("changefreq")).thenReturn(elem2Change);

        NodeList elem3Loc = mockNodeList("https://education.byu.edu/deans");
        NodeList elem3Change = mockNodeList("weekly");

        Mockito.when(elem3.getElementsByTagName("loc")).thenReturn(elem3Loc);
        Mockito.when(elem3.getElementsByTagName("changefreq")).thenReturn(elem3Change);

        NodeList elem4Loc = mockNodeList("https://education.byu.edu/employee_portal");
        NodeList elem4Change = mockNodeList("weekly");

        Mockito.when(elem4.getElementsByTagName("loc")).thenReturn(elem4Loc);
        Mockito.when(elem4.getElementsByTagName("changefreq")).thenReturn(elem4Change);

        NodeList elem5Loc = mockNodeList("https://education.byu.edu/deans/financial-aid");
        NodeList elem5Change = mockNodeList("weekly");

        Mockito.when(elem5.getElementsByTagName("loc")).thenReturn(elem5Loc);
        Mockito.when(elem5.getElementsByTagName("changefreq")).thenReturn(elem5Change);

        NodeList elem6Loc = mockNodeList("https://education.byu.edu/advisement/overview/directory");
        NodeList elem6Change = mockNodeList("monthly");

        Mockito.when(elem6.getElementsByTagName("loc")).thenReturn(elem6Loc);
        Mockito.when(elem6.getElementsByTagName("changefreq")).thenReturn(elem6Change);

        Mockito.when(nList.getLength()).thenReturn(6);
        Mockito.when(nList.item(0)).thenReturn(elem1);
        Mockito.when(nList.item(1)).thenReturn(elem2);
        Mockito.when(nList.item(2)).thenReturn(elem3);
        Mockito.when(nList.item(3)).thenReturn(elem4);
        Mockito.when(nList.item(4)).thenReturn(elem5);
        Mockito.when(nList.item(5)).thenReturn(elem6);

        return nList;
    }

    private NodeList mockNodeList(String value) {
        NodeList list = mock(NodeList.class);
        org.w3c.dom.Node node = mockNode(value);

        Mockito.when(list.getLength()).thenReturn(1);
        Mockito.when(list.item(0)).thenReturn(node);

        return list;
    }

    private org.w3c.dom.Node mockNode(String value) {
        org.w3c.dom.Node mockNode = mock(org.w3c.dom.Node.class);

        Mockito.when(mockNode.getTextContent()).thenReturn(value);

        return mockNode;
    }
}

package uk.ac.cam.echo2016.multinarrative;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

public class NarrativeInstanceTest {
	ArrayList<Narrative> narrList = new ArrayList<Narrative>();
	ArrayList<Node> nodeList = new ArrayList<Node>();

	public NarrativeInstanceTest() {
		/**
		 * The structure created here is what is found in the Visual Basic diagram on the Google Drive.
		 */
		nodeList.add(new SynchronizationNode("syncStart")); // 0
		nodeList.add(new SynchronizationNode("syncEnd")); 	// 1
		nodeList.add(new SynchronizationNode("sync1")); 	// 2
		nodeList.add(new SynchronizationNode("sync2")); 	// 3
		nodeList.add(new SynchronizationNode("sync3")); 	// 4
		nodeList.add(new SynchronizationNode("sync4")); 	// 5
		nodeList.add(new ChoiceNode("choiceMike1")); 		// 6
		nodeList.add(new ChoiceNode("choiceSam1")); 		// 7
		nodeList.add(new ChoiceNode("choiceSarah1")); 		// 8
		nodeList.add(new ChoiceNode("choiceChris1")); 		// 9
		nodeList.add(new ChoiceNode("choiceJessica1")); 	// 10

		Narrative tempNarr;

		tempNarr = new Narrative("narrMike1",nodeList.get(0),nodeList.get(6));
		nodeList.get(0).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrMike2",nodeList.get(6),nodeList.get(1));
		nodeList.get(6).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrMike3",nodeList.get(6),nodeList.get(4));
		nodeList.get(6).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrMike4",nodeList.get(4),nodeList.get(1));
		nodeList.get(4).options.add(tempNarr);
		narrList.add(tempNarr);
		
		tempNarr = new Narrative("narrSam1",nodeList.get(0),nodeList.get(7));
		nodeList.get(0).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrSam2",nodeList.get(7),nodeList.get(4));
		nodeList.get(7).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrSam3",nodeList.get(4),nodeList.get(1));
		nodeList.get(4).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrSam4",nodeList.get(7),nodeList.get(5));
		nodeList.get(7).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrSam5",nodeList.get(5),nodeList.get(1));
		nodeList.get(5).options.add(tempNarr);
		narrList.add(tempNarr);
		
		tempNarr = new Narrative("narrSarah1",nodeList.get(0),nodeList.get(8));
		nodeList.get(0).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrSarah2",nodeList.get(8),nodeList.get(4));
		nodeList.get(8).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrSarah3",nodeList.get(4),nodeList.get(1));
		nodeList.get(4).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrSarah4",nodeList.get(8),nodeList.get(2));
		nodeList.get(8).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrSarah5",nodeList.get(2),nodeList.get(4));
		nodeList.get(2).options.add(tempNarr);
		narrList.add(tempNarr);
		
		tempNarr = new Narrative("narrChris1",nodeList.get(0),nodeList.get(9));
		nodeList.get(0).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrChris2",nodeList.get(9),nodeList.get(2));
		nodeList.get(9).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrChris3",nodeList.get(2),nodeList.get(1));
		nodeList.get(2).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrChris4",nodeList.get(9),nodeList.get(3));
		nodeList.get(9).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrChris5",nodeList.get(3),nodeList.get(1));
		nodeList.get(3).options.add(tempNarr);
		narrList.add(tempNarr);
		
		tempNarr = new Narrative("narrJessica1",nodeList.get(0),nodeList.get(3));
		nodeList.get(0).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrJessica2",nodeList.get(3),nodeList.get(10));
		nodeList.get(3).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrJessica3",nodeList.get(10),nodeList.get(5));
		nodeList.get(10).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrJessica4",nodeList.get(5),nodeList.get(1));
		nodeList.get(5).options.add(tempNarr);
		narrList.add(tempNarr);
		tempNarr = new Narrative("narrJessica5",nodeList.get(10),nodeList.get(1));
		nodeList.get(10).options.add(tempNarr);
		narrList.add(tempNarr);
	}
	@Test
	public void testNodeStructure() {
		NarrativeInstanceTest test = new NarrativeInstanceTest();
		NarrativeTemplate template = new NarrativeTemplate();
		template.narratives.addAll(test.narrList);
		template.nodes.addAll(test.nodeList);
		template.start = template.getNode("syncStart");
		
		template.getNode("choiceMike1").createProperties();
		template.getNode("choiceMike1").getProperties().putBoolean("ChoicePropertyCopiedCorrectly", true);
		template.getNode("sync1").createProperties();
		template.getNode("sync1").getProperties().putBoolean("SyncPropertyCopiedCorrectly", true);
		
		assertEquals(24, template.narratives.size());
		assertEquals(11, template.nodes.size());
		assertEquals(template.getNarrative("narrSarah5").getEnd().getIdentifier(), "sync3");

		NarrativeInstance instance = new NarrativeInstance(template);
		
		assertTrue("Check Choice properties copied correctly", instance.getNodeProperties("choiceMike1").containsKey("ChoicePropertyCopiedCorrectly"));
		assertTrue("Check Sync properties copied correctly", instance.getNodeProperties("sync1").containsKey("SyncPropertyCopiedCorrectly"));

		assertEquals("Checking correct number of narratives: ", 24, instance.narratives.size());
		assertEquals("Checking correct number of nodes: ", 11, instance.nodes.size());
		assertEquals("Checking \"narrSarah5\" connects to \"sync3\":", instance.getNarrative("narrSarah5").getEnd()
				.getIdentifier(), "sync3");
		assertTrue("Checking \"choiceJessica1\" has narrative \"narrJessica3\":", instance.getNode(
				"choiceJessica1").options.contains(instance.getNarrative("narrJessica3")));

		Narrative narr = instance.getNarrative("narrBob1");
		assertNull(narr);

		instance.kill("narrMike1");
		assertEquals(21, instance.narratives.size());// Implementation not finished
		
		System.out.println("Test Completed");
	}
}
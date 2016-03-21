package func.nn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A node in a <code> Layer </code> of a <code> NeuralNetwork </code> implementation that is linked 
 * to a list of neurons in following and/or previous layers through <code> Link </code> objects.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Neuron implements Serializable {
	
	/**
	 * The list of in links along which values are passed from neurons in the preceding layer 
	 * to this neuron typically after being multiplied by each link's weight value and whose sum is 
	 * stored in the activation variable.
	 * @see Layer
	 * @see Link
	 */
	private List inLinks;
	
	/**
	 * The list of out links along which the activation value of this neuron is passed to
	 * the following layer's neurons typically after being multiplied by each link's weight value.
	 * @see Layer
	 * @see Link
	 */
	private List outLinks;

	/**
	 * The value of this neuron that is typically passed to the neurons in the
	 * following layer along outward links after being multiplied by their weight values.
	 * @see Layer
	 * @see Link
	 */
	private double activation;

	/**
	 * Creates a new neuron with initially empty lists of outward
	 * and inward links.
	 * @see Link 
	 */
	public Neuron() {
		inLinks = new ArrayList();
		outLinks = new ArrayList();
	}

	/**
	 * Retrieves the value of this neuron typically to be sent along links
	 * to the following layer's neurons except for those in the output layer 
	 * where the value represents a final result of the network.
	 * @return the value of this neuron
	 * @see LayeredNetwork
	 * @see Layer
	 * @see Link
	 * @see Neuron#activation
	 */
	public double getActivation() {
		return activation;
	}
	
	/**
	 * Sets the value of this neuron.
	 * @param value the new activation value
	 * @see Neuron#activation
	 */
	public void setActivation(double value) {
		this.activation = value;
	}

	/**
	 * Retrieves the input links of this neuron from which it receives
	 * values to determine its activation value.
	 * @return the list of incoming links
	 * @see Link
	 * @see Neuron#inLinks
	 */
	public List getInLinks() {
		return inLinks;
	}
	
	/**
	 * Retrieves the number of in links which also represent the number of neurons
	 * in the previous layer from which this neuron receives values.
	 * @return the number of in links
	 * @see Layer
	 * @see Link
	 * @see Neuron#inLinks
	 */
	public int getInLinkCount() {
		return inLinks.size();
	}
	
	/**
	 * Retrieves the ith incoming link.
	 * @param i the in link index
	 * @return the ith incoming link
	 * @see Link
	 * @see Neuron#inLinks
	 */
	public Link getInLink(int i) {
		return (Link) inLinks.get(i);
	}

	/**
	 * Retrieves the output links of this neuron along which it sends
	 * values to neurons in the following layer often multiplied by each link's weight.
	 * @return the output links
	 * @see Layer
	 * @see Link
	 * @see Neuron#outLinks
	 */
	public List getOutLinks() {
		return outLinks;
	}

	/**
	 * Adds an in link.
	 * @param link the link to add
	 * @see Link
	 * @see Neuron#inLinks
	 */
	public void addInLink(Link link) {
		inLinks.add(link);
	}

	/**
	 * Adds an out link.
	 * @param link the link to add
	 * @see Link
	 * @see Neuron#outLinks
	 */
	public void addOutLink(Link link) {
		outLinks.add(link);
	}

	/**
	 * Retrieves the number of out links which also represent the number of neurons
	 * in the following layer to which this neuron sends values.
	 * @return the number of out links
	 * @see Layer
	 * @see Link
	 * @see Neuron#outLinks
	 */
	public int getOutLinkCount() {
		return outLinks.size();
	}
	
	/**
	 * Retrieves the ith outgoing link.
	 * @param i the out link index
	 * @return the ith outgoing link
	 * @see Link
	 * @see Neuron#outLinks
	 */
	public Link getOutLink(int i) {
		return (Link) outLinks.get(i);
	}
	
	/**
	 * Removes the given in link.
	 * @param link the link to remove
	 * @see Link
	 * @see Neuron#inLinks
	 */
	public void removeInLink(Link link) {
		inLinks.remove(link);
	}
	
	/**
	 * Removes the given out link.
	 * @param link the link to remove
	 * @see Link
	 * @see Neuron#outLinks
	 */
	public void removeOutLink(Link link) {
		outLinks.remove(link);
	}

	/**
	 * Creates a new link object during which a random weight is initially calculated
	 * that is typically used to multiply activation values sent between neurons in
	 * consecutive layers.
	 * @return the link to use
	 * @see Link#Link()
	 */
	public Link createLink() {
		return new Link();
	}
	
	/**
	 * Connects this neuron to the given new output neuron using a link object along which
	 * values are passed typically after being multiplied by the link's weight value.
	 * @param node the neuron to connect to
	 * @see Link#setInNode(Neuron)
	 * @see Link#setOutNode(Neuron)
	 * @see Neuron#createLink()
	 * @see Neuron#addOutLink(Link)
	 * @see Neuron#addInLink(Link)
	 */
	public void connect(Neuron node) {
		Link link = createLink();
		link.setInNode(this);
		link.setOutNode(node);		
		addOutLink(link);
		node.addInLink(link);
	}
	
	/**
	 * Disconnects this neuron from the given neuron by eliminating the link.
	 * connecting the two.
	 * @param node the neuron to disconnect from
	 * @see Link#getInNode()
	 * @see Neuron#getInLinks()
	 * @see Neuron#removeInLink(Link)
	 */
	public void disconnect(Neuron node) {
		for (int i = 0; i < outLinks.size(); i++) {
			Link link = (Link) outLinks.get(i);
			if (link.getInNode() == this) {
				outLinks.remove(link);
				break;
			}
		}
		for (int i = 0; i < node.getInLinks().size(); i++) {
			Link link = (Link) node.getInLinks().get(i);
			if (link.getInNode() == this) {
				node.removeInLink(link);
				break;
			}
		}
	}

}

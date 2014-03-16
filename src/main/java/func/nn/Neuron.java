package func.nn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A node in a neural network implementation
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Neuron implements Serializable {
	
	/**
	 * The list of in links
	 */
	private List inLinks;
	
	/**
	 * The list of out links
	 */
	private List outLinks;

	/**
	 * The value of the node
	 */
	private double activation;

	/**
	 * Make a new node
	 */
	public Neuron() {
		inLinks = new ArrayList();
		outLinks = new ArrayList();
	}

	/**
	 * Get the error value
	 * @return the error
	 */
	public double getActivation() {
		return activation;
	}
	
	/**
	 * Set the value
	 * @param value the new value
	 */
	public void setActivation(double value) {
		this.activation = value;
	}

	/**
	 * Get the input links
	 * @return the list of links
	 */
	public List getInLinks() {
		return inLinks;
	}
	
	/**
	 * Get the number of in links
	 * @return the number of in links
	 */
	public int getInLinkCount() {
		return inLinks.size();
	}
	
	/**
	 * Get in link number i
	 * @param i the in link index
	 * @return the link
	 */
	public Link getInLink(int i) {
		return (Link) inLinks.get(i);
	}

	/**
	 * Get the output links
	 * @return the output links
	 */
	public List getOutLinks() {
		return outLinks;
	}

	/**
	 * Add a in link
	 * @param link the link to add
	 */
	public void addInLink(Link link) {
		inLinks.add(link);
	}

	/**
	 * Add a out link
	 * @param link the link to add
	 */
	public void addOutLink(Link link) {
		outLinks.add(link);
	}

	/**
	 * Get the number of out links
	 * @return the number of out links
	 */
	public int getOutLinkCount() {
		return outLinks.size();
	}
	
	/**
	 * Get out link number i
	 * @param i the out link index
	 * @return the link
	 */
	public Link getOutLink(int i) {
		return (Link) outLinks.get(i);
	}
	
	/**
	 * Remove the given in link
	 * @param link the link to remove
	 */
	public void removeInLink(Link link) {
		inLinks.remove(link);
	}
	
	/**
	 * Remove the given out link
	 * @param link the link to remove
	 */
	public void removeOutLink(Link link) {
		outLinks.remove(link);
	}

	/**
	 * Create a new link
	 * @return the link to use
	 */
	public Link createLink() {
		return new Link();
	}
	
	/**
	 * Connect this node to the given node
	 * @param node the node to connect to
	 */
	public void connect(Neuron node) {
		Link link = createLink();
		link.setInNode(this);
		link.setOutNode(node);		
		addOutLink(link);
		node.addInLink(link);
	}
	
	/**
	 * Disconnect this node from the given node
	 * @param node the node to disconnect from
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

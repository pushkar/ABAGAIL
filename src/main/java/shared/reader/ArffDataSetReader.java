package shared.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
/**
 * Class to read in data from a ARFF file
 * @author Jarvis Johnson <https://github.com/Magicjarvis>
 * @author Alex Linton <https://github.com/lexlinton>
 * @date 2013-03-05
 */
public class ArffDataSetReader extends DataSetReader {

	private final String DATA_TAG = "@data";
	private final String ATTRIBUTE_TAG = "@attribute";
	private final int SPLIT_LIMIT = 3;

	
	public ArffDataSetReader(String file) {
		super(file);
	}

	@Override
	public DataSet read() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
    		List<Map<String, Double>> attributes = processAttributes(in);
    		Instance[] instances = processInstances(in, attributes);
    		DataSet set = new DataSet(instances);
    		set.setDescription(new DataSetDescription(set));
    		return set;
		} finally {
    		// don't forget to close the buffer
    		in.close();
		}
	}

	/**
	 * Parses the buffer in to a map attribute->
	 * @param in Buffer to read from
	 * @return 
	 * @throws IOException
	 */
	private List<Map<String, Double>> processAttributes(BufferedReader in)
		throws IOException {
		String line = in.readLine();
		List<Map<String, Double>> attributes
			= new ArrayList<Map<String, Double>>();
		while (line != null && line.toLowerCase().indexOf(DATA_TAG) == -1) {
			if (!line.isEmpty() && line.charAt(0) != '%') {
				String[] parts = line.split("\\s", SPLIT_LIMIT);
				if (parts[0].equalsIgnoreCase(ATTRIBUTE_TAG)) {
					// process any attribute values
				    //NOTE: for REAL and INTEGER types, this will do nothing but those types are handled
				    // in processInstances
					String[] values = parts[2].replaceAll(" |\\{|\\}|'","").split(",");
					double id = 0.0;
					Map<String, Double> valMap = new HashMap<String, Double>();
					for (String s : values) {
					    s = s.trim(); //trim off whitespace
						valMap.put(s, id++);
					}
					attributes.add(valMap);
				}
			}
			line = in.readLine();
		}
		return attributes;
	}

	private Instance[] processInstances(BufferedReader in,
			List<Map<String, Double>> valueMaps) throws IOException {
		List<Instance> instances = new ArrayList<Instance>();
		String line = in.readLine();
		Pattern pattern = Pattern.compile("[ ,]+");
		while (line != null) {
			if (!line.isEmpty() && line.charAt(0) != '%') {
				String[] values = pattern.split(line.trim());
				double[] ins = new double[values.length];
				for (int i = 0; i < values.length; i++) {
				    //some values are single quoted (especially in datafiles bundled
				    // with weka)
					String v = values[i].replaceAll("'", "");
					// defaulting to 0 if attribute value unknown.
					double d = 0;
					try {
	                	d = Double.parseDouble(v);
	                }
	                catch(NumberFormatException e){
	                	if (valueMaps.get(i).containsKey(v)) {
							d = valueMaps.get(i).get(v);
						}
	                }
					ins[i] = d;
				}
				Instance i = new Instance(ins);
				instances.add(i);
			}
			line = in.readLine();
		}
		return instances.toArray(new Instance[instances.size()]);
	}

}

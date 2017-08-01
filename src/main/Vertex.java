public class Vertex {
	
	private String _name;
	private double _weight;
	public Vertex (String name, double weight) {
		_name = name;
		_weight = weight;
	}

	public String getName() {
		return _name;
	}
	public double getWeight() {
		return _weight;
	}
}
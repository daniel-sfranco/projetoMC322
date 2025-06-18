package search;

public class SearchManager {
    private Researcher researcher;

    public SearchManager(Researcher researcher){
        this.researcher = researcher;
    }

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}
}

package kicktraq.crawler.entities;

public class KickProject {
	public static final String ARCHIVED_TYPE = "ARCHIVED";
	public static final String ACTIVE_TYPE = "ACTIVE";
	
	private static final int PROJECTS_STRING_LENGTH = 10;
	private static final String PROJECTS_STRING = "/projects/";
	private String m_projectName;
	private String m_projectKickStarterURL;
	private String m_projectType;
	
	public KickProject() {
		this("",ARCHIVED_TYPE);
	}
	
	public KickProject(String projectURL, String projectType) {
		this.m_projectKickStarterURL = projectURL;
		
		m_projectType = projectType;
		
		this.m_projectName = parseProjectName(projectURL);
	}
	
	private String parseProjectName(String projectURL) {
		int indexOfProjects = projectURL.indexOf(PROJECTS_STRING);
		String projectName = projectURL.substring(indexOfProjects + PROJECTS_STRING_LENGTH);
		return projectName;
	}

	public String getProjectName() {
		return m_projectName;
	}
	
	void setProjectName(String projectName) {
		this.m_projectName = projectName;
	}
	public String getProjectKickStarterURL() {
		return m_projectKickStarterURL;
	}
	
	void setProjectKickStarterURL(String projectKickStarterURL) {
		this.m_projectKickStarterURL = projectKickStarterURL;
	}

	public String getProjectType() {
		return m_projectType;
	}

	void setProjectType(String projectType) {
		this.m_projectType = projectType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((m_projectName == null) ? 0 : m_projectName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KickProject other = (KickProject) obj;
		if (m_projectName == null) {
			if (other.m_projectName != null)
				return false;
		} else if (!m_projectName.equals(other.m_projectName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KickProject [m_projectName=" + m_projectName + ", m_projectType=" + m_projectType
				+ ", m_projectKickStarterURL=" + m_projectKickStarterURL + "]";
	}
}

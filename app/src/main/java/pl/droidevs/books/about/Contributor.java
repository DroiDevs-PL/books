package pl.droidevs.books.about;

import java.util.ArrayList;
import java.util.List;

import pl.droidevs.books.R;

/**
 * Created by micha on 13.03.2018.
 */

public class Contributor {

    private final String name;
    private final String nick;
    private final int resourceImage;
    private final String gitHubUrl;


    public Contributor(String name, String nick, int resourceImage, String gitHubUrl) {
        this.name = name;
        this.nick = nick;
        this.resourceImage = resourceImage;
        this.gitHubUrl = gitHubUrl;
    }

    public String getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public String getGitHubUrl() {
        return gitHubUrl;
    }


    public static List<Contributor> getTeam() {
        List<Contributor> team = new ArrayList<>();

        team.add(new Contributor("Natalia Jastrzębska",
                "nani92",
                R.drawable.contributor_nani,
                "https://github.com/nani92"));

        team.add(new Contributor("Karol Lisiewicz",
                "klisiewicz",
                R.drawable.contributor_klisiewicz,
                "https://github.com/klisiewicz"));

        team.add(new Contributor("Michał Bachta",
                "MichaelB-pl",
                R.drawable.contributor_mb,
                "https://github.com/MichaelB-pl"));

        team.add(new Contributor("Adam Świderski",
                "asvid",
                R.drawable.contributor_asvid,
                "https://github.com/asvid"));

        return team;
    }
}

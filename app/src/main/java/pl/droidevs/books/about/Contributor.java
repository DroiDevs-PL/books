package pl.droidevs.books.about;

import android.support.annotation.Nullable;

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
    private final String githubUrl;
    private final String linkedInUrl;
    private final String googlePlayStoreUrl;
    private final String websiteUrl;


    private Contributor(String name, String nick, int resourceImage, String githubUrl,
                        @Nullable String linkedInUrl, @Nullable String googlePlayStoreUrl, @Nullable String websiteUrl) {
        this.name = name;
        this.nick = "@" + nick;
        this.resourceImage = resourceImage;
        this.githubUrl = githubUrl;
        this.linkedInUrl = linkedInUrl;
        this.googlePlayStoreUrl = googlePlayStoreUrl;
        this.websiteUrl = websiteUrl;
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

    public String getGithubUrl() {
        return githubUrl;
    }

    @Nullable
    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    @Nullable
    public String getGooglePlayStoreUrl() {
        return googlePlayStoreUrl;
    }

    @Nullable
    public String getWebsiteUrl() {
        return websiteUrl;
    }


    public static List<Contributor> getTeam() {
        List<Contributor> team = new ArrayList<>();

        team.add(new Contributor("Natalia Jastrzębska",
                "nani92",
                R.drawable.contributor_nani,
                "https://github.com/nani92",
                null,
                "https://play.google.com/store/apps/dev?id=8034018404241334903",
                null));

        team.add(new Contributor("Karol Lisiewicz",
                "klisiewicz",
                R.drawable.contributor_klisiewicz,
                "https://github.com/klisiewicz",
                "http://www.linkedin.com/in/karol-lisiewicz",
                "https://play.google.com/store/apps/developer?id=Karol+Lisiewicz",
                null));

        team.add(new Contributor("Michał Bachta",
                "MichaelB-pl",
                R.drawable.contributor_mb,
                "https://github.com/MichaelB-pl",
                "https://www.linkedin.com/in/micha%C5%82-bachta/",
                null,
                null));

        team.add(new Contributor("Adam Świderski",
                "asvid",
                R.drawable.contributor_asvid,
                "https://github.com/asvid",
                null,
                null,
                null));

        return team;
    }
}

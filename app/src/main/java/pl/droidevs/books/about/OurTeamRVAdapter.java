package pl.droidevs.books.about;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidevs.books.R;

/**
 * Created by micha on 13.03.2018.
 */

public class OurTeamRVAdapter extends RecyclerView.Adapter<OurTeamRVAdapter.OurTeamRVAdapterViewHolder> {
    private final Context context;
    private final teamClickListener teamClickListener;

    private final List<Contributor> team = Contributor.getTeam();

    public interface teamClickListener {
        void onItemClick(int index);
    }

    public OurTeamRVAdapter(Context context, teamClickListener teamClickListener) {
        this.context = context;
        this.teamClickListener = teamClickListener;
    }

    @Override
    public OurTeamRVAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_contributor, parent, false);
        return new OurTeamRVAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OurTeamRVAdapterViewHolder holder, int position) {
        holder.bind(team.get(position));
    }

    @Override
    public int getItemCount() {
        return team.size();
    }

    class OurTeamRVAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_nick)
        TextView tvNick;

        public OurTeamRVAdapterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> teamClickListener.onItemClick(getAdapterPosition()));
        }

        public void bind(@NonNull Contributor contributor) {
            tvName.setText(contributor.getName());
            tvNick.setText(contributor.getNick());

            Glide.with(context).load(contributor.getResourceImage()).into(ivAvatar);
        }
    }
}

package pl.droidevs.books.library;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidevs.books.R;
import pl.droidevs.books.model.Book;

final class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.BookViewHolder> {
    private List<Book> books = new ArrayList<>();

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new BookViewHolder(layoutInflater.inflate(R.layout.list_item_book, parent, false));
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.bind(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setItems(@NonNull final List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    static final class BookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_book_title)
        TextView tvBookTitle;

        public BookViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(final Book book) {
            tvBookTitle.setText(book.getTitle());
        }
    }
}

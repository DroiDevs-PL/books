package pl.droidevs.books.library;

import android.content.Context;
import static com.bumptech.glide.Priority.HIGH;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidevs.books.R;
import pl.droidevs.books.model.Book;
import pl.droidevs.books.model.BookId;

final class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.BookViewHolder> {
    private List<Book> books = new ArrayList<>();
    private BookItemClickListener bookItemClickListener;

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_book, parent, false);

        return new BookViewHolder(itemView, bookItemClickListener);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        holder.bind(books.get(position));

        holder.ivBook.setTransitionName("image_" + position);
        holder.tvBookTitle.setTransitionName("title_" + position);
        holder.tvBookAuthor.setTransitionName("author_" + position);
        holder.shadowView.setTransitionName("shadow_" + position);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    void setItemClickListener(@Nullable BookItemClickListener bookItemClickListener) {
        this.bookItemClickListener = bookItemClickListener;
    }

    void setItems(@NonNull final List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public void removeItem(int position, BookItemRemoveListener listener) {
        listener.onBookRemoved(this.books.get(position));
        this.books.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Book book, int position) {
        this.books.add(position, book);
        notifyItemInserted(position);
    }

    @FunctionalInterface
    public interface BookItemClickListener {
        void onBookClicked(@NonNull View view, @NonNull String bookId, @NonNull Integer index);
    }

    @FunctionalInterface
    public interface BookItemRemoveListener {
        void onBookRemoved(@NonNull Book book);
    }

    static final class BookViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_book_title)
        TextView tvBookTitle;
        @BindView(R.id.tv_book_author)
        TextView tvBookAuthor;
        @BindView(R.id.iv_book)
        ImageView ivBook;
        @Nullable
        private BookId bookId;

        @BindView(R.id.shadow_view)
        View shadowView;

        private BookViewHolder(View itemView, BookItemClickListener onClickListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                if (onClickListener != null && bookId != null) {
                    onClickListener.onBookClicked(itemView, bookId.getId(), getAdapterPosition());
                }
            });
        }

        void bind(@NonNull Book book) {
            bookId = book.getId();

            tvBookTitle.setText(book.getTitle());
            tvBookAuthor.setText(book.getAuthor());

            Glide.with(ivBook.getContext())
                    .load(book.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_book)
                            .priority(HIGH)
                    )
                    .into(ivBook);
        }
    }
}

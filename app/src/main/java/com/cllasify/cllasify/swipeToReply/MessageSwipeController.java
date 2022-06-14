package com.cllasify.cllasify.swipeToReply;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.R;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public final class MessageSwipeController extends ItemTouchHelper.Callback {
    private Drawable imageDrawable;
    private Drawable shareRound;
    private RecyclerView.ViewHolder currentItemViewHolder;
    private View mView;
    private float dX;
    private float replyButtonProgress;
    private long lastReplyButtonAnimationTime;
    private boolean swipeBack;
    private boolean isVibrate;
    private boolean startTracking;
    private final Context context;
    private final SwipeControllActions swipeControllerActions;

    public int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        Intrinsics.checkParameterIsNotNull(recyclerView, "recyclerView");
        Intrinsics.checkParameterIsNotNull(viewHolder, "viewHolder");
        View var10001 = viewHolder.itemView;
        Intrinsics.checkExpressionValueIsNotNull(var10001, "viewHolder.itemView");
        this.mView = var10001;
        Drawable var3 = this.context.getDrawable(R.drawable.ic_launcher_background);
        if (var3 == null) {
            Intrinsics.throwNpe();
        }

        this.imageDrawable = var3;
        var3 = this.context.getDrawable(R.drawable.ic_launcher_background);
        if (var3 == null) {
            Intrinsics.throwNpe();
        }

        this.shareRound = var3;
        return ItemTouchHelper.Callback.makeMovementFlags(0, 8);
    }

    public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
        Intrinsics.checkParameterIsNotNull(recyclerView, "recyclerView");
        Intrinsics.checkParameterIsNotNull(viewHolder, "viewHolder");
        Intrinsics.checkParameterIsNotNull(target, "target");
        return false;
    }

    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        Intrinsics.checkParameterIsNotNull(viewHolder, "viewHolder");
    }

    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (this.swipeBack) {
            this.swipeBack = false;
            return 0;
        } else {
            return super.convertToAbsoluteDirection(flags, layoutDirection);
        }
    }

    public void onChildDraw(@NotNull Canvas c, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Intrinsics.checkParameterIsNotNull(c, "c");
        Intrinsics.checkParameterIsNotNull(recyclerView, "recyclerView");
        Intrinsics.checkParameterIsNotNull(viewHolder, "viewHolder");
        if (actionState == 1) {
            this.setTouchListener(recyclerView, viewHolder);
        }

        View var10000 = this.mView;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mView");
        }

        if (var10000.getTranslationX() < (float) this.convertTodp(130) || dX < this.dX) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            this.dX = dX;
            this.startTracking = true;
        }

        this.currentItemViewHolder = viewHolder;
        this.drawReplyButton(c);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private final void setTouchListener(RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        recyclerView.setOnTouchListener((View.OnTouchListener) (new View.OnTouchListener() {
            public final boolean onTouch(View $noName_0, MotionEvent event) {
                MessageSwipeController var10000 = MessageSwipeController.this;
                Intrinsics.checkExpressionValueIsNotNull(event, "event");
                var10000.swipeBack = event.getAction() == 3 || event.getAction() == 1;
                if (MessageSwipeController.this.swipeBack && Math.abs(MessageSwipeController.access$getMView$p(MessageSwipeController.this).getTranslationX()) >= (float) MessageSwipeController.this.convertTodp(100)) {
                    MessageSwipeController.this.swipeControllerActions.showReplyUi(viewHolder.getAdapterPosition());
                }

                return false;
            }
        }));
    }

    private final void drawReplyButton(Canvas canvas) {
        if (this.currentItemViewHolder != null) {
            View var10000 = this.mView;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mView");
            }

            float translationX = var10000.getTranslationX();
            long newTime = System.currentTimeMillis();
            long dt = Math.min(17L, newTime - this.lastReplyButtonAnimationTime);
            this.lastReplyButtonAnimationTime = newTime;
            boolean showing = translationX >= (float) this.convertTodp(30);
            if (showing) {
                if (this.replyButtonProgress < 1.0F) {
                    this.replyButtonProgress += (float) dt / 180.0F;
                    if (this.replyButtonProgress > 1.0F) {
                        this.replyButtonProgress = 1.0F;
                    } else {
                        var10000 = this.mView;
                        if (var10000 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("mView");
                        }

                        var10000.invalidate();
                    }
                }
            } else if (translationX <= 0.0F) {
                this.replyButtonProgress = 0.0F;
                this.startTracking = false;
                this.isVibrate = false;
            } else if (this.replyButtonProgress > 0.0F) {
                this.replyButtonProgress -= (float) dt / 180.0F;
                if (this.replyButtonProgress < 0.1F) {
                    this.replyButtonProgress = 0.0F;
                } else {
                    var10000 = this.mView;
                    if (var10000 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("mView");
                    }

                    var10000.invalidate();
                }
            }

//            int alpha = false;
            float scale = 0.0F;
            int alpha;
            if (showing) {
                scale = this.replyButtonProgress <= 0.8F ? 1.2F * (this.replyButtonProgress / 0.8F) : 1.2F - 0.2F * ((this.replyButtonProgress - 0.8F) / 0.2F);
                alpha = (int) Math.min(255.0F, (float) 255 * (this.replyButtonProgress / 0.8F));
            } else {
                scale = this.replyButtonProgress;
                alpha = (int) Math.min(255.0F, (float) 255 * this.replyButtonProgress);
            }

            Drawable var13 = this.shareRound;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("shareRound");
            }

            var13.setAlpha(alpha);
            var13 = this.imageDrawable;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("imageDrawable");
            }

            var13.setAlpha(alpha);
            if (this.startTracking && !this.isVibrate) {
                var10000 = this.mView;
                if (var10000 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mView");
                }

                if (var10000.getTranslationX() >= (float) this.convertTodp(100)) {
                    var10000 = this.mView;
                    if (var10000 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("mView");
                    }

                    var10000.performHapticFeedback(3, 2);
                    this.isVibrate = true;
                }
            }

            var10000 = this.mView;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mView");
            }

            int var14;
            if (var10000.getTranslationX() > (float) this.convertTodp(130)) {
                var14 = this.convertTodp(130) / 2;
            } else {
                var10000 = this.mView;
                if (var10000 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mView");
                }

                var14 = (int) (var10000.getTranslationX() / (float) 2);
            }

            int x = var14;
            var10000 = this.mView;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mView");
            }

            var14 = var10000.getTop();
            View var10001 = this.mView;
            if (var10001 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mView");
            }

            float y = (float) (var14 + var10001.getMeasuredHeight() / 2);
            var13 = this.shareRound;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("shareRound");
            }

//            var13.setColorFilter((ColorFilter)(new PorterDuffColorFilter(ContextCompat.getColor(this.context, 500089), PorterDuff.Mode.MULTIPLY)));
            var13 = this.shareRound;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("shareRound");
            }

            var13.setBounds((int) ((float) x - (float) this.convertTodp(18) * scale), (int) (y - (float) this.convertTodp(18) * scale), (int) ((float) x + (float) this.convertTodp(18) * scale), (int) (y + (float) this.convertTodp(18) * scale));
            var13 = this.shareRound;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("shareRound");
            }

            var13.draw(canvas);
            var13 = this.imageDrawable;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("imageDrawable");
            }

            var13.setBounds((int) ((float) x - (float) this.convertTodp(12) * scale), (int) (y - (float) this.convertTodp(11) * scale), (int) ((float) x + (float) this.convertTodp(12) * scale), (int) (y + (float) this.convertTodp(10) * scale));
            var13 = this.imageDrawable;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("imageDrawable");
            }

            var13.draw(canvas);
            var13 = this.shareRound;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("shareRound");
            }

            var13.setAlpha(255);
            var13 = this.imageDrawable;
            if (var13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("imageDrawable");
            }

            var13.setAlpha(255);
        }
    }

    private int convertTodp(int pixel) {

        return AndroidUtils.INSTANCE.dp((float) pixel, this.context);
    }

    public MessageSwipeController(@NotNull Context context, @NotNull SwipeControllActions swipeControllerActions) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(swipeControllerActions, "swipeControllerActions");
//        super();
        this.context = context;
        this.swipeControllerActions = swipeControllerActions;
    }

    // $FF: synthetic method
    public static final View access$getMView$p(MessageSwipeController $this) {
        View var10000 = $this.mView;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mView");
        }

        return var10000;
    }

    // $FF: synthetic method
    public static final void access$setMView$p(MessageSwipeController $this, View var1) {
        $this.mView = var1;
    }
}

package com.example.imgtest.repository.config;

import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

public class PageToRecordConverter implements Subscriber<Page<LicenseRecord>>, Publisher<List<LicenseRecord>> {

    private static final int DEFAULT_DYNAMODB_RESULT_LIMIT = 20;
    public List<LicenseRecord> output = new ArrayList<>(DEFAULT_DYNAMODB_RESULT_LIMIT);;
    private Subscription upstreamSubscription;
    private Subscriber<? super List<LicenseRecord>> subscriber;

    @Override
    public void subscribe(Subscriber<? super List<LicenseRecord>> subscriber) {
        this.subscriber = subscriber;
        subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                upstreamSubscription.request(1);
            }

            @Override
            public void cancel() {
                upstreamSubscription.cancel();
            }
        });
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.upstreamSubscription = subscription;
    }

    @Override
    public void onNext(Page<LicenseRecord> playerRecordPage) {
        output.addAll(playerRecordPage.items());
    }

    @Override
    public void onError(Throwable throwable) {
        subscriber.onError(new RuntimeException("PageToRecordConverter failure"));
    }

    @Override
    public void onComplete() {
        subscriber.onNext(output);
        subscriber.onComplete();
    }
}

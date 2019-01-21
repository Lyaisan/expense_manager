package com.lalmeeva.expense.data.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lalmeeva.expense.utils.CommunicationUtils;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * Created by lalmeeva on 26.08.2017.
 */

@Module
public class BaseRepositoryModule {

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CommunicationUtils.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(CommunicationUtils.RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
//                .registerTypeAdapter(ConfigurationResponse.ConfigPayload.class, new ConfigurationResponse.ConfigPayloadDeserializer())
//                .registerTypeAdapter(AuthenticationResponse.AuthPayload.class, new AuthenticationResponse.AuthPayloadDeserializer())
                .create();
    }

    @Provides
    @Singleton
    RestServiceFactory provideRestServiceFactory(OkHttpClient client, Gson gson/*, @Named("user_data") SharedPreferences userSharedPreferences*/){
        return new RestServiceFactory(client, gson/*, userSharedPreferences*/);
    }

    @Provides
    public BillRepository provideBillRepository(RestServiceFactory restServiceFactory) {
        return new BillRepository(restServiceFactory);
    }
}

package com.example.newsapp.constants

///
object Constants {
    const val BASE_URL = "https://newsdata.io/api/1/"
    const val DEVELOPMENT_BASE_URL = "http://172.16.0.45:8089/api/"
    const val TESTING_BASE_URL = "http://172.16.0.45:8087/api/"
    const val PRODUCTION_BASE_URL = "https://dummyjson.com/"
    const val HOME_PARAM = "news?apikey=pub_1728381cae76dd8c6ff1271dbf7c887d5270b"
    val API_TOKEN = "pub_1728381cae76dd8c6ff1271dbf7c887d5270b"


    const val CATEGORY = "category"
    const val SEARCH_PARAM = "q"
    const val CATEGORY_PREF = "TOPIC_VALUES"
    const val USER_NAME = "user_name"

    /*DB*/
    const val DB_NAME = "categoryTable"
    const val EVENT_ID = "event_id"
    const val EVENT_TITLE = "event_title"
    const val EVENT_DESC = "event_description"
    const val EVENT_URL = "event_url"


    const val NEWS_DATA = "news_data"
    const val COPY_NEWS = "copy"


}
package com.zhengpu.aiuilibrary.iflytekbean;

import java.util.List;

/**
 * sayid ....
 * Created by wengmf on 2017/11/23.
 */

public class AirDataBean {


    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * airData : 52
         * airQuality : 良
         * city : 深圳
         * date : 2017-11-23
         * dateLong : 1511366400
         * exp : {"ct":{"expName":"穿衣指数","level":"较舒适","prompt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"}}
         * humidity : 48%
         * lastUpdateTime : 2017-11-23 08:00
         * pm25 : 35
         * temp : 17
         * tempRange : 13℃ ~ 19℃
         * weather : 多云
         * weatherType : 1
         * wind : 无持续风向微风
         * windLevel : 0
         */

        private int airData;
        private String airQuality;
        private String city;
        private String date;
        private int dateLong;
        private ExpBean exp;
        private String humidity;
        private String lastUpdateTime;
        private String pm25;
        private int temp;
        private String tempRange;
        private String weather;
        private int weatherType;
        private String wind;
        private int windLevel;

        public int getAirData() {
            return airData;
        }

        public void setAirData(int airData) {
            this.airData = airData;
        }

        public String getAirQuality() {
            return airQuality;
        }

        public void setAirQuality(String airQuality) {
            this.airQuality = airQuality;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getDateLong() {
            return dateLong;
        }

        public void setDateLong(int dateLong) {
            this.dateLong = dateLong;
        }

        public ExpBean getExp() {
            return exp;
        }

        public void setExp(ExpBean exp) {
            this.exp = exp;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }

        public String getTempRange() {
            return tempRange;
        }

        public void setTempRange(String tempRange) {
            this.tempRange = tempRange;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public int getWeatherType() {
            return weatherType;
        }

        public void setWeatherType(int weatherType) {
            this.weatherType = weatherType;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public int getWindLevel() {
            return windLevel;
        }

        public void setWindLevel(int windLevel) {
            this.windLevel = windLevel;
        }

        public static class ExpBean {
            /**
             * ct : {"expName":"穿衣指数","level":"较舒适","prompt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"}
             */

            private CtBean ct;

            public CtBean getCt() {
                return ct;
            }

            public void setCt(CtBean ct) {
                this.ct = ct;
            }

            public static class CtBean {
                /**
                 * expName : 穿衣指数
                 * level : 较舒适
                 * prompt : 建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。
                 */

                private String expName;
                private String level;
                private String prompt;

                public String getExpName() {
                    return expName;
                }

                public void setExpName(String expName) {
                    this.expName = expName;
                }

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }

                public String getPrompt() {
                    return prompt;
                }

                public void setPrompt(String prompt) {
                    this.prompt = prompt;
                }
            }
        }
    }


}

from flask import Flask, request, jsonify
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
import time;
app = Flask(__name__)

analyzer = SentimentIntensityAnalyzer()

def isNegative(title):
    sentiment_scores = analyzer.polarity_scores(title)
    return sentiment_scores['compound'] <=0 or abs(sentiment_scores['neg']) > sentiment_scores['pos']
        
@app.route('/analyze-sentiment', methods=['POST'])
def analyze_sentiment():
    # data here takes in a jsonarray of strings

    start = time.time()
    data = request.get_json()
# ========================================= 

    boolean_list = [isNegative(title) for title in data]
    response = {
        'sentiments': boolean_list
    }

    end = time.time()

    print(end - start)
    return jsonify(response)

# =========================================
    
if __name__ == '__main__':
    app.run(debug=True)


import { Component } from "react";

/**
 * 概要: 画像読み込み時にimgタグへwidth, height属性を100%サイズで自動付与する.
 */
class Form extends Component {

  // 画像ID.
  imageId = "";

  /**
   * コンストラクタ.
   * 部品化を想定しIDをユニーク自動採番する.
   */
  constructor() {
    super();
    this.setUniqueStr();
    this.state = { imageUrl: "", width: 0, height: 0 };
  }

  /**
   * ユニークIDを自動採番する.
   */
  setUniqueStr() {
    var strong = 50;
    this.imageId = new Date().getTime().toString(16) + Math.floor(strong * Math.random()).toString(16)
  }

  /**
   * 表示するURL入力イベント.
   * @param {*} e 画像URL入力テキスト
   */
  onClick(e) {
    const imageUrl = document.getElementById("txt01").value;
    document.getElementById(this.imageId).removeAttribute("width");
    document.getElementById(this.imageId).removeAttribute("height");
    console.log("#onClick imageUrl:" + imageUrl);
    this.setState({ imageUrl: imageUrl });
  }

  /**
   * 画像読み込みイベント.
   * @param {*} e 画像
   */
  onLoad(e) {
    console.log("#onLoad imageUrl:" + this.state.imageUrl);
    const width = e.target.width;
    const height = e.target.height;
    console.log("width:" + width + ", height:" + height);
    this.setState({ imageUrl: this.state.imageUrl, width: width, height: height});
    e.target.setAttribute("width", width);
    e.target.setAttribute("height", height);
  }

  /**
   * レンダリング処理.
   * @returns 試験フォーム
   */
  render() {
    return (
      <form>
        画像URL
        <input id="txt01" type="text" size="100"></input>
        <input type="button" value="load image" onClick={this.onClick.bind(this)}></input>
        <p>
          width: {this.state.width}, height: {this.state.height}
        </p>
        <div>
          <img id={this.imageId}
            src={this.state.imageUrl}
            onLoad={this.onLoad.bind(this)}
          >
          </img>
        </div>
      </form>
    );
  }
}

export default Form;

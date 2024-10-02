import React from "react";
import { useNavigate } from "react-router-dom";
import "./Category.css";

// 이미지들을 모두 import
import dAbstract from '../assets/d-abstract-holographic-form.png';
import cosmeticComposition from '../assets/cosmetic-composition.png';
import pinkHeadphones from '../assets/pink-headphones-floating.png';
import movieCamera from '../assets/movie-video-camera.png';
import foodBasket from '../assets/Food-basket-with-groceries.png';
import tShirtMockup from '../assets/t-shirt-mockup.png';
import gameController from '../assets/game-controller.png';
import traditional from '../assets/traditional.png';

export const Category = ({setCartegory}) => {
    const navigate = useNavigate();

    return (
        <div className="category-wrapper">
            <div className="category-buttons-container">
                <button className="category-button-2" onClick={() => setCartegory("전체")}>
                    <div className="category-margin">
                        <div className="category-background">
                            <img className="category-image" alt="D abstract" src={dAbstract} />
                        </div>
                    </div>
                    <div className="category-container">
                        <div className="category-text-wrapper">전체</div>
                    </div>
                </button>
                <button className="category-button-2" onClick={() => setCartegory("뷰티")}>
                    <div className="category-margin">
                        <div className="category-image-wrapper">
                            <img className="category-image-small" alt="Cosmetic composition" src={cosmeticComposition} />
                        </div>
                    </div>
                    <div className="category-container">
                        <div className="category-text-wrapper">뷰티</div>
                    </div>
                </button>
                <button className="category-button-2" onClick={() => setCartegory("K-POP")}>
                    <div className="category-margin">
                        <div className="category-background">
                            <img className="category-image" alt="Pink headphones" src={pinkHeadphones} />
                        </div>
                    </div>
                    <div className="category-container">
                        <div className="category-text-wrapper">K - POP</div>
                    </div>
                </button>
                <button className="category-button-2" onClick={() => setCartegory("K-콘텐츠")}>
                    <div className="category-margin">
                        <div className="category-background">
                            <img className="category-image" alt="Movie video camera" src={movieCamera} />
                        </div>
                    </div>
                    <div className="category-container">
                        <div className="category-text-wrapper">K- 콘텐츠</div>
                    </div>
                </button>
                <button className="category-button-2" onClick={() => setCartegory("음식")}>
                    <div className="category-margin">
                        <div className="category-background">
                            <img className="category-image" alt="Food basket with" src={foodBasket} />
                        </div>
                    </div>
                    <div className="category-container">
                        <div className="category-text-wrapper">음식</div>
                    </div>
                </button>
                <button className="category-button-2" onClick={() => setCartegory("문화재")}>
                    <div className="category-margin">
                        <div className="category-background">
                            <img className="category-image" alt="Traditional" src={traditional} />
                        </div>
                    </div>
                    <div className="category-container">
                        <div className="category-text-wrapper">문화재</div>
                    </div>
                </button>
                <button className="category-button-2" onClick={() => setCartegory("패션")}>
                    <div className="category-margin">
                        <div className="category-background">
                            <img className="category-image" alt="T shirt mockup" src={tShirtMockup} />
                        </div>
                    </div>
                    <div className="category-container">
                        <div className="category-text-wrapper">패션</div>
                    </div>
                </button>
                <button className="category-button-2" onClick={() => setCartegory("게임")}>
                    <div className="category-margin">
                        <div className="category-background">
                            <img className="category-image" alt="Game controller" src={gameController} />
                        </div>
                    </div>
                    <div className="category-container">
                        <div className="category-text-wrapper">게임</div>
                    </div>
                </button>
            </div>
        </div>
    );
};

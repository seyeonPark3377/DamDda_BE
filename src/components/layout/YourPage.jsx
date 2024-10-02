import React from "react";
import { Header } from "./Header";
import { Footer } from "./Footer";
import './YourPage.module.css';  // CSS 파일을 import

export function YourPage() {
    return (
        <>
            <Header />

            <div className="container">
                {/* 페이지 내용 */}
                <h1>Welcome to My Page</h1>
                <div className="card">
                    <input type="text" placeholder="Input Field" className="input" />
                    <button className="button">Submit</button>
                </div>
            </div>

            <Footer />
        </>
    );
}

export default YourPage;

import React from 'react';
import Carousel from 'react-bootstrap/Carousel';
import ExampleCarouselImage from '../../assets/fresh-sale.png';
import 'bootstrap/dist/css/bootstrap.min.css';

export function CarouselComponent() {
  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', margin: '20px 0' }}> 
      {/* 위아래로 마진 20px */}
      <Carousel fade interval={4000} style={{ maxWidth: '70%', width: '1920px', height: 'auto' }}> 
        {/* interval={5000} 추가로 5초마다 자동 전환 */}
        <Carousel.Item>
          <img
            src={ExampleCarouselImage}
            alt="First slide"
            style={{ width: '100%', height: '300px', objectFit: 'cover', borderRadius: 30 }} 
          />
          <Carousel.Caption>
            <h3>First slide label</h3>
            <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
          <img
            src={ExampleCarouselImage}
            alt="Second slide"
            style={{ width: '100%', height: '300px', objectFit: 'cover', borderRadius: 30 }} 
          />
          <Carousel.Caption>
            <h3>Second slide label</h3>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
          <img
            src={ExampleCarouselImage}
            alt="Third slide"
            style={{ width: '100%', height: '300px', objectFit: 'cover', borderRadius: 30 }} 
          />
          <Carousel.Caption>
            <h3>Third slide label</h3>
            <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur.</p>
          </Carousel.Caption>
        </Carousel.Item>
      </Carousel>
    </div>
  );
}

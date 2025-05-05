import React from "react"
import { createRoot } from 'react-dom/client'
import {redClass} from './style/app.module.css'
import './style/test.css';

function App() {
  return (
    <div>
      <h1 className={redClass}>Using CSS module</h1>
      <p className="test_class">Using CSS as an usual way</p>
    </div>
  )
}

const container = document.getElementById('root')
const root = createRoot(container) // createRoot(container!) if you use TypeScript

root.render(<App />)
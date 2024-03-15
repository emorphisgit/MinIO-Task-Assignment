import { configureStore } from "@reduxjs/toolkit";
import AlertPopUpSlice from "./slices/AlertPopUpSlice";


const store = configureStore({
    reducer: {
        alert: AlertPopUpSlice
    }
})

export default store;
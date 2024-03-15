import { PayloadAction, createSlice } from "@reduxjs/toolkit";


interface AlertState {
    alertMessage: string;
    openAlert: boolean;
    alertVariant: any | undefined;
}


const initialState: AlertState = {
    openAlert: false,
    alertMessage: 'success',
    alertVariant: '',
}

const alertSlice = createSlice({
    name: 'alert',
    initialState: initialState,
    reducers: {
        setAlertData: (state, action: PayloadAction<AlertState>) => {
            state.openAlert = true
            state.alertMessage = action.payload.alertMessage;
            state.alertVariant = action.payload.alertVariant;
        },
        clearAlertData: (state) => {
            state.openAlert = false;
            state.alertMessage = 'success';
            state.alertVariant = '';
        }
    }
})

export const {setAlertData, clearAlertData} = alertSlice.actions;
export default alertSlice.reducer;
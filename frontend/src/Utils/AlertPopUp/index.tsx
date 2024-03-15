import { Alert, Snackbar } from '@mui/material'
import React from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { clearAlertData } from '../../Redux/slices/AlertPopUpSlice';


const AlertPopUp = () => {
    const dispatch = useDispatch();
    const { alertVariant, alertMessage, openAlert } = useSelector((state: any) => state.alert)
    const handleAlert = (
        event: React.SyntheticEvent | Event,
        reason?: string
    ) => {
        if (reason === "clickaway") {
            return;
        }
        dispatch(clearAlertData());
    };
    return (
        <Snackbar
            open={openAlert}
            autoHideDuration={5000}
            onClose={handleAlert}
            anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        >
            <Alert
                elevation={6}
                variant="filled"
                onClose={handleAlert}
                severity={alertVariant || "success"}
            >
                {typeof alertMessage === 'string' ? alertMessage : ''}
            </Alert>
        </Snackbar>
    )
}

export default AlertPopUp
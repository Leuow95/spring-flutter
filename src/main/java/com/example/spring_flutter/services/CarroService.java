package com.example.spring_flutter.services;

import com.amazonaws.services.s3.AmazonS3;
import com.example.spring_flutter.domain.carro.Carro;
import com.example.spring_flutter.domain.carro.CarroRequestDto;
import com.example.spring_flutter.domain.pessoa.Pessoa;
import com.example.spring_flutter.exceptions.CarroNotFound;
import com.example.spring_flutter.exceptions.FailToUploadImage;
import com.example.spring_flutter.exceptions.PessoaNotFound;
import com.example.spring_flutter.repositories.CarroRepository;
import com.example.spring_flutter.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private AmazonS3 s3;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public Carro create(UUID pessoaId, CarroRequestDto carroRequestDto){
        String imgUrl = null;

        if(carroRequestDto.foto() !=null){
            imgUrl = uploadImage(carroRequestDto.foto());
        }

        Pessoa pessoa = pessoaRepository.findById(pessoaId).orElseThrow(
                ()-> new PessoaNotFound("Pessoa não encontrada com o id" + pessoaId));

        Carro newCarro = new Carro();

        newCarro.setPessoa(pessoa);
        newCarro.setMarca(carroRequestDto.marca());
        newCarro.setModelo(carroRequestDto.modelo());
        newCarro.setPlaca(carroRequestDto.placa());
        newCarro.setFotoUrl(imgUrl);

        return carroRepository.save(newCarro);
    }

    public String uploadImage(MultipartFile multipartFile){
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try{
            File file = this.convertMultipartToFile(multipartFile);
            s3.putObject(bucketName, fileName, file);
            file.delete();
            return s3.getUrl(bucketName, fileName).toString();
        }catch (Exception e){
            throw new FailToUploadImage(e.getMessage());
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException{
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public List<Carro> getCarrosByPessoaId(UUID pessoaId){
        return carroRepository.getCarrosByPessoaId(pessoaId);

    }

    public Carro getCarroById(UUID carroId){
        return carroRepository.findById(carroId).orElseThrow(
                ()-> new CarroNotFound("Carro não encontrado"));

    }

    public Carro updateCarro(CarroRequestDto carroRequestDto, UUID carroId){

        Carro existingCarro = getCarroById(carroId);

        if (carroRequestDto.marca() != null) {
            existingCarro.setMarca(carroRequestDto.marca());
        }
        if (carroRequestDto.modelo() != null) {
            existingCarro.setModelo(carroRequestDto.modelo());
        }
        if (carroRequestDto.placa() != null) {
            existingCarro.setPlaca(carroRequestDto.placa());
        }
        if (carroRequestDto.foto() != null && !carroRequestDto.foto().isEmpty()) {
            String photoUrl = uploadImage(carroRequestDto.foto());
            existingCarro.setFotoUrl(photoUrl);
        }
        return carroRepository.save(existingCarro);
    }

    public void deleteCarro(UUID id){
        if(carroRepository.existsById(id)){
            carroRepository.deleteById(id);
        }else{
            throw new CarroNotFound("Carro não encontrado");
        }

    }
}
